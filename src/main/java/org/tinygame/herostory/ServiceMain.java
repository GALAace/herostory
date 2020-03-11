package org.tinygame.herostory;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;


/**
 * 服务器主入口
 *
 *http://cdn0001.afrxvk.cn/hero_story/demo/step010/index.html?serverAddr=127.0.0.1:12345&userId=1
 */
public class ServiceMain {
    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); //处理连接
        EventLoopGroup workerGroup = new NioEventLoopGroup(); //处理业务

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,workerGroup);
        bootstrap.channel(NioServerSocketChannel.class); //服务器信道处理方式
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() { //客户端信道处理方式
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(
                        new HttpServerCodec(),//Http服务器编码
                        new HttpObjectAggregator(65535),//内容长度限制
                        new WebSocketServerProtocolHandler("/websocket"),//websocket协议处理器
                        new GameMsgHandle()//自定义消息处理器
                );
            }
        });
        try {
            //绑定12345端口
            ChannelFuture future = bootstrap.bind(12345).sync();
            if(future.isSuccess()){
                System.out.println("服务启动成功");
            }
            //等待服务器通信结束
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
