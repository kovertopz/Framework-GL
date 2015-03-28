package net.smert.frameworkgl.examples.networklinerepeater;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class LineRepeaterClientHandler extends ChannelInboundHandlerAdapter {

    private final ByteBuf firstMessage;

    public LineRepeaterClientHandler() {
        String msg = "Is there an echo in here?\r\n";
        firstMessage = Unpooled.buffer(128);
        firstMessage.writeBytes(msg.getBytes());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(firstMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) {
        t.printStackTrace();
        ctx.close();
    }

}
