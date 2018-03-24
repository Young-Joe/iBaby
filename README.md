# iBaby
####产品介绍

iBaby是用于向宝爸宝妈介绍疫苗,并针对宝宝该接种的疫苗进行提醒通知的应用.

##### 主要功能

- 首页:向用户展示一类疫苗和二类疫苗
- 头像:登录后可以进行头像设置和裁剪
- 爱宝:登录后补充宝宝信息可以查看宝宝该接种什么疫苗
- 提醒:开启宝宝接种疫苗的提醒服务

##### 补充说明

- 所有数据均来源于网络,仅供参考.还请各位宝爸宝妈在对宝宝接种疫苗前向医生进行确认!" 
- 用户在进行设置过宝宝出生日期,并开启提醒后,会在疫苗接种前三天进行通知提醒.但......由于国内厂商均有应用后台杀死机制,iBaby考虑用户体验以及耗电性本身没有进行线程保活,采取了向系统日历写入行程的方式来进行提醒通知.
- 如有建议欢迎提issue,祝各位宝宝健康成长~

#### 开发介绍

##### 原型设计

使用墨刀进行原型绘制

##### 程序设计

App Module 采样kotlin

Library Module 直接引用在用的依赖

项目架构采用mvvm模式.RxJava2+ViewModule+Greendao3.2(ps....其实写起来发现业务比较简单时间紧也就懒得遵循构建了,后期会重新架构)

##### 图标资源

[阿里矢量图库](http://www.iconfont.cn/?spm=a313x.7781069.1998910419.d4d0a486a)

##### 截图

![Screenshot_2018-03-24-14-22-53-198_com.joe.ibaby](https://ws3.sinaimg.cn/large/006tNc79ly1fpo1zfkfmzj30u01hck2s.jpg)

![Screenshot_2018-03-24-14-22-36-955_com.joe.ibaby](https://ws1.sinaimg.cn/large/006tNc79ly1fpo1zhppt0j30u01hcgu5.jpg)

![Screenshot_2018-03-24-14-22-41-823_com.joe.ibaby](https://ws1.sinaimg.cn/large/006tNc79ly1fpo1zhatlkj30u01hcjxs.jpg)

![Screenshot_2018-03-24-14-22-56-101_com.joe.ibaby](https://ws4.sinaimg.cn/large/006tNc79ly1fpo1zgd8chj30u01hctdg.jpg)