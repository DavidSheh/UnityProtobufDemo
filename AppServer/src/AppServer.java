import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class AppServer {
	//private static Log log = LogFactory.getLog(HttpServer.class);  
    
    public static void main(String[] args) throws Exception {  
        AppServer server = new AppServer();  
        System.out.println("服务已启动...");  
        server.start(8082);  
    }  
      
    public void start(int port) throws Exception {  
        //配置服务端的NIO线程组  
        EventLoopGroup bossGroup = new NioEventLoopGroup();  
        EventLoopGroup workerGroup = new NioEventLoopGroup();  
        try {  
            ServerBootstrap b = new ServerBootstrap();  
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)  
                    .childHandler(new ChannelInitializer<SocketChannel>() {  
                                @Override  
                                public void initChannel(SocketChannel ch) throws Exception {  
                                	// 半包的处理
                                    ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                                    // 需要解码的目标类
                                    ch.pipeline().addLast(new ProtobufDecoder(HotMessage.Message.getDefaultInstance()));

                                    ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());

                                    ch.pipeline().addLast(new ProtobufEncoder());
                                    
                                	ch.pipeline().addLast(new ServerHandler());  
                                }  
                            }).option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);  
            //绑定端口，同步等待成功  
            ChannelFuture f = b.bind(port).sync();  
            //等待服务端监听端口关闭  
            f.channel().closeFuture().sync();  
        } finally {  
            //优雅退出，释放线程池资源  
            workerGroup.shutdownGracefully();  
            bossGroup.shutdownGracefully();  
        }  
    }  
}
