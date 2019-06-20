package com.miu30.common.ftp;

/**
 * 作者：wanglei on 2019/6/20.
 * 邮箱：forwlwork@gmail.com
 */
public class Demo {

    public static void main(String[] args) {
        RemoteLocalPair pair = new RemoteLocalPair("/FTP-Server/test.bmp"    //需要下载文件的地址
                , ""        //下载文件保存到本地所在的目录，如：Environment.getExternalStorageDirectory().getPath()
                , ""       //下载文件保存到本地的名称
        );

        new Thread(new FtpDownloader("10.150.15.237"              // ftp 服务器ip
                , "wang"                                        // 用户名
                , "123"                                       // 密码
                , 21                                              // ftp 服务器端口
                , new GeneralDownloadInfo(pair))
        ).start();
    }

}
