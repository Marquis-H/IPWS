#include <stdio.h>   
#include <sys/ioctl.h>
#include <unistd.h>
#include <fcntl.h> 
#include <string.h>
#include <iostream>  
#include <sstream> 
#include <stdlib.h>
#include <ctime>   
#include <linux/i2c.h>   
#include <linux/i2c-dev.h>   

#include </usr/local/curl/include/curl/curl.h>
//正则
#include <boost/regex.hpp>
#define MAX_BUF     65536
struct i2c_rdwr_ioctl_data rdwrdata;
using namespace std;
using namespace boost;
//正则匹配字段
regex expression("^seid:(.*)<br >open_hum:(.*)<br >end_hum:(.*)<br >open_time:(.*)<br >end_time:(.*)<br >pri:(.*)<br >time:");
char wr_buf[MAX_BUF+1];
int  wr_index;

//写入数据
size_t write_data( void *buffer, size_t size, size_t nmemb, void *userp ){
	int segsize = size * nmemb;

	if ( wr_index + segsize > MAX_BUF ) {
		*(int *)userp = 1;
		return 0;
	}
	memcpy( (void *)&wr_buf[wr_index], buffer, (size_t)segsize );
	wr_index += segsize;
	wr_buf[wr_index] = 0;
	return segsize;
}

int main(int argc, char *argv[]) {
	int i, j=0, err, fd; 
	int h, t, b;
	//模数/数模转换模块上各模块的地址位
	char wordaddr;
	char *rdbuf1;
	char *rdbuf2;
	int bytenum;
	//湿度
	string humdata;
	//温度
	string tempdata;
	//光强
	string beamdata;
	//水泵开始状态，1为开启，0为关闭
	string wp;
	string time_now;

	CURL *curl;
	CURLcode ret;

	cmatch what;
	int  wr_error;
	//打开i2c设备文件
	fd = open("/dev/i2c-1",O_RDWR);
	//轮询
	while(1){ 
		wr_error = 0;
		wr_index = 0;
		if(j == 2){
			curl = curl_easy_init();
			if (!curl) {
				printf("couldn't init curl\n");
				return 0;
			}
			//curl获取接口数据，设置的参数，seid=001为指定设备号
			curl_easy_setopt( curl, CURLOPT_URL, "http://ipws.sinaapp.com/senddata/send.php?seid=001" );
			curl_easy_setopt( curl, CURLOPT_WRITEDATA, (void *)&wr_error );
			curl_easy_setopt( curl, CURLOPT_WRITEFUNCTION, write_data );
			ret = curl_easy_perform( curl );
			if ( ret == 0 ){
				if(regex_match(wr_buf, what, expression)){}
			}
			curl_easy_cleanup( curl );
		}
		//判断启动数据
	    if(argc < 1){  
	        printf("please input as:");  
	        printf("./rat24 [read byte addr] [read num of byte]\n");  
	        return -1;  
	    }
		wordaddr = j;  
		bytenum = 4;  
	  
	    rdwrdata.msgs = (struct i2c_msg *)malloc(2*sizeof(struct i2c_msg));  
	    if(!rdwrdata.msgs){  
	        printf("rdwrdata.msgs malloc failed!\n");  
	        return -1;  
	    }     
  
	    rdbuf1 = ( char *)malloc(sizeof(char));
	    //通关传感器获取到的数据，存放地址
	    rdbuf2 = ( char *)malloc(bytenum*sizeof(char));
	    //内存  
	    if((!rdbuf1) || (!rdbuf2)){  
	        printf("rdbuf malloc failed!\n");  
	        return -1;  
	    }  
  		//写入模数/数模转换模块设备地址
	    rdwrdata.nmsgs = 2;  
	      
	    (rdwrdata.msgs[0]).addr = 0x48;  
	    (rdwrdata.msgs[0]).len = 1;  
	    (rdwrdata.msgs[0]).flags = 0;  
	    (rdwrdata.msgs[0]).buf = (__u8*)rdbuf1;  
	    (rdwrdata.msgs[0]).buf[0] = wordaddr;  
	          
	    (rdwrdata.msgs[1]).addr = 0x48;  
	    (rdwrdata.msgs[1]).len = bytenum;  
	    (rdwrdata.msgs[1]).flags = I2C_M_RD;  
	    (rdwrdata.msgs[1]).buf = (__u8*)rdbuf2;

	    if(fd < 0){
	        printf("i2c device open failed!\n");  
	        return -1;    
	    }  
      
	    err = ioctl(fd, I2C_SLAVE_FORCE, 0x50);  
	    if(err < 0){  
	        printf("ioctl failed\n");  
	        return -1;  
	    }  
	      
	    err = ioctl(fd, I2C_RDWR, &rdwrdata);  
	    if(err < 0){  
	        printf("ioctl msgs error, error number:%d\n", err);  
	        return -1;  
	    }  
	    //当前时间
		time_t now_time;
		now_time = time(NULL);
		stringstream ss;
		ss<<now_time;
		ss>>time_now;
	 
	    for(i = 2; i < bytenum; i++) {}
	    //湿度数据
	    if((rdbuf2[2]==rdbuf2[3])&&(wordaddr==2)){
			h=rdbuf2[2];
			//int转string
			stringstream ss;
			ss<<h;
			ss>>humdata;
			if(((what[3]<=humdata)&&what[6]=="0")||(what[4]<=time_now&&time_now<=what[5])&&(what[6]=="2")){
			    system("sh /home/work/control/on.sh");
			    wp="1";
			}else if(humdata<=what[2]&&what[6]=="0"||(what[5]<=time_now)&&(what[6]=="2")){
			    system("sh /home/work/control/off.sh");
			    wp="0";
			}else if((((what[4]<=time_now&&time_now<=what[5])&&what[3]<=humdata)||what[3]<=humdata)&&what[6]=="1"){
			    system("sh /home/work/control/on.sh");
				wp="1";
			}else if((humdata<=what[2]||what[5]<=time_now)&&what[6]=="1"){
			    system("sh /home/work/control/off.sh");
				wp="0";
			}
			//打印出当前湿度数据
		    cout<<"hum:"+humdata<<endl;

		}
		//光强数据
	    if((rdbuf2[2]==rdbuf2[3])&&(wordaddr==0)){
		    b=rdbuf2[2];
		    stringstream ss;
			ss<<b;
			ss>>beamdata;
			//打印出当前光强数据
			cout<<"beam:"+beamdata<<endl;
		}
		//温度数据
	    if((rdbuf2[2]==rdbuf2[3])&&(wordaddr==1)){
		    t=rdbuf2[2];
		    stringstream ss;
            ss<<t;
            ss>>tempdata;
            //打印出当前温度数据
            cout<<"temp:"+tempdata<<endl;
	    }
		j++;
		//最后一步将当前温湿度光强，水泵状态上传到云数据库
		if(j==3){
			j=0;
			//生成post参数
			string s = "curl -d 'seid=001&hum=";
			s += humdata;
			s += "&temp=";
			s += tempdata;
			s += "&wp=";
			s += wp;
			s += "&beam=";
			s += beamdata;
			s += "&time=";
			s += time_now; 
			//数据库接口地址
			s += "' 'http://ipws.sinaapp.com/getdata/put.php'";
			system(s.c_str());
		}
    }
    //关闭i2c设备
	close(fd);
	return 0;  
} 
