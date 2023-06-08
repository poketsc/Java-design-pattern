import adapter.*;
import aop.AopBrowser;
import decorator.*;
import facade.Ftp;
import facade.Reader;
import facade.SftpClient;
import facade.Writer;
import observer.Button;
import observer.IButtonListener;
import proxy.Browser;
import proxy.BrowserProxy;
import proxy.IBrowser;
import singleton.AClazz;
import singleton.BClazz;
import singleton.SocketClient;
import strategy.*;

import java.util.Base64;
import java.util.concurrent.atomic.AtomicLong;

public class Main {
    public static void main(String[] args) {

//        싱글톤 테스트
        AClazz aClazz = new AClazz();
        BClazz bClazz = new BClazz();

        SocketClient aClient = aClazz.getSocketClient();
        SocketClient bClient = bClazz.getSocketClient();

        System.out.println(aClient.equals(bClient));

//        어댑터 테스트
        HairDryer hairDryer = new HairDryer();
        connect(hairDryer);

        Cleaner cleaner = new Cleaner();

        Electronic110V adapter = new SocketAdapter(cleaner);
        connect(adapter);

        AirConditioner airConditioner = new AirConditioner();

        Electronic110V airAdapter = new SocketAdapter(airConditioner);
        connect(airAdapter);

//        프록시 테스트
/*        Browser browser = new Browser("www.naver.com");
        browser.show();
        browser.show();
        browser.show();
        browser.show();
        browser.show();*/

        IBrowser browser = new BrowserProxy("www.naver.com");
        browser.show();
        browser.show();
        browser.show();
        browser.show();
        browser.show();

        AtomicLong start = new AtomicLong();
        AtomicLong end = new AtomicLong();

        IBrowser aopBrowser = new AopBrowser("www.naver.com",
                ()-> {
                    System.out.println("before");
                    start.set(System.currentTimeMillis());
                },
                ()->{
                    long now = System.currentTimeMillis();
                    end.set(now - start.get());
                });

        aopBrowser.show();
        System.out.println("loading time : " + end.get());

        aopBrowser.show();
        System.out.println("loading time : " + end.get());

//        데코레이터 테스트
        ICar audi = new Audi(1000);
        audi.showPrice();

//        a3
        ICar audi3 = new A3(audi, "A3");
        audi3.showPrice();
//        a4
        ICar audi4 = new A4(audi, "A4");
        audi4.showPrice();
//        a5
        ICar audi5 = new A5(audi, "A5");
        audi5.showPrice();

//        옵저버 테스트
        Button button = new Button("버튼");
        button.addListener(new IButtonListener() {
            @Override
            public void clickEvent(String event) {
                System.out.println(event);
            }
        });
        button.click("메세지 전달: click1");
        button.click("메세지 전달: click2");
        button.click("메세지 전달: click3");
        button.click("메세지 전달: click4");
        button.click("메세지 전달: click5");

//        파사드 패턴 테스트
//        Ftp ftpClient = new Ftp("www.foo.co.kr", 22, "/home/etc");
//        ftpClient.connect();
//        ftpClient.moveDirectory();
//
//        Writer writer = new Writer("text.tmp");
//        writer.fileConnect();
//        writer.write();
//        Reader reader = new Reader("text.tmp");
//        reader.fileConnect();
//        reader.fileRead();
//
//        reader.fileDisconnect();
//        writer.fileDisconnect();
//        ftpClient.disConnect();

        SftpClient sftpClient = new SftpClient("www.foo.co.kr", 22, "/home/etc", "text.tmp");
        sftpClient.connect();

        sftpClient.write();
        sftpClient.read();

        sftpClient.disConnect();

//        strategy 패턴 테스트
        Encoder encoder = new Encoder();

        EncodingStrategy base64 = new Base64Strategy();

        EncodingStrategy normal = new NormalStrategy();

        String message = "hello java";
        encoder.setEncodingStrategy(base64);
        String base64Result = encoder.getMessage(message);
        System.out.println(base64Result);

        encoder.setEncodingStrategy(normal);
        String normalResult = encoder.getMessage(message);
        System.out.println(normalResult);

        EncodingStrategy appendStrategy = new AppendStrategy();
        encoder.setEncodingStrategy(appendStrategy);
        String appendResult = encoder.getMessage(message);
        System.out.println(appendResult);
    }

    public static void connect(Electronic110V electronic110V) {
        electronic110V.powerOn();
    }
}