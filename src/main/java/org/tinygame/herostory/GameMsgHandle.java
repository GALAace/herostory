package org.tinygame.herostory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

/**
 * 游戏消息处理器
 */
public class GameMsgHandle extends SimpleChannelInboundHandler {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println("收到客户端消息,o："+ o);

        //websocket二进制消息会通过HttpServerCodec解码成BinaryWebSocketFrame对象
        BinaryWebSocketFrame frame = (BinaryWebSocketFrame) o;
        ByteBuf byteBuf = frame.content();

        //拿到真实的字节数组并打印
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        System.out.println("收到字节：");
        for(byte b : bytes){
            System.out.print(b);
            System.out.print(",");
        }

        System.out.println();
    }
}
