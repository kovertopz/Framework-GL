package net.smert.frameworkgl.examples.networklinerepeater;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
@ChannelHandler.Sharable
public class LineRepeaterServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        int totalBytes = buf.readableBytes();
        if (totalBytes > 2) {
            byte last = buf.getByte(totalBytes - 1);
            byte nextToLast = buf.getByte(totalBytes - 2);
            if ((nextToLast == 13) && (last == 10)) {
                ctx.write(msg);
            }
        }
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
