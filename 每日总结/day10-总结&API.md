# 任务
	1.nginx的安装
	2.nginx实现反向代理、负载均衡
	3.Sso系统工程搭建
	
------

# 心得体会&学到的东西
	1.nginx的安装：
		1).由于nginx是c语言开发的，所以在linux环境需要yum install gcc-c++
		2).第三方的开发包。
		   PCRE
			PCRE(Perl Compatible Regular Expressions)是一个Perl库，包括 perl 兼容的正则表达式库。nginx
			的http模块使用pcre来解析正则表达式，所以需要在linux上安装pcre库。
		   yum install -y pcre pcre-devel
			注：pcre-devel是使用pcre开发的一个二次开发库。nginx也需要此库。
		   zlib
			zlib库提供了很多种压缩和解压缩的方式，nginx使用zlib对http包的内容进行gzip，所以需要在linux上安装
			zlib库。
		  yum install -y zlib zlib-devel
	
	   	  openssl
			OpenSSL 是一个强大的安全套接字层密码库，囊括主要的密码算法、常用的密钥和证书封装管理功能及SSL协议，
			并提供丰富的应用程序供测试或其它目的使用。
			nginx不仅支持http协议，还支持https（即在ssl协议上传输http），所以需要在linux安装openssl库。
		  yum install -y openssl openssl-devel

	2.nginx的启动和关闭，在sbin目录下,启动./nginx ，关闭./nginx -s stop,或者./nginx -s quit，刷新配置文件
	  ./nginx -s reload

	3.可以在EditPulus上面，通过FTP链接到虚拟机，编辑一些配置文件，更加方便

	4.负载均衡的权重：
	upstream tomcat1{
		server 192.168.25.133:8080 weight=1;#默认等于1
		server 192.168.25.133:8082 weight=2;
	}
	2次8082，1次8080
		
# 遇到的问题
	N/A
