import java.io.ByteArrayInputStream;
import java.util.Date;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter{
//	 private static Log log = LogFactory.getLog(ServerHandler.class);
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		super.handlerAdded(ctx);
		System.out.println(ctx.channel().id()+"进来了");
	}
	
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		super.handlerRemoved(ctx);
		System.out.println(ctx.channel().id()+"离开了");
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
		System.out.println("服务器接收到消息！");
		HotMessage.Message msg = (HotMessage.Message) obj;
		System.out.println("---消息内容" + msg.getMsg().toStringUtf8());
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		ctx.close();
	}
}