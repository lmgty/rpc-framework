import com.yufa.xz.ly.remoting.transport.netty.server.NettyServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * register service and start netty server
 * @author LiuYe
 * @data 2020/9/16
 */
@ComponentScan("com.yufa.xz.ly")
public class NettyServerMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyServerMain.class);
        NettyServer nettyServer = applicationContext.getBean(NettyServer.class);

        nettyServer.start();
    }
}
