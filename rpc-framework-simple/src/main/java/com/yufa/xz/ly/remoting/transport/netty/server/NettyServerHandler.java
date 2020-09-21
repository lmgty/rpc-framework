package com.yufa.xz.ly.remoting.transport.netty.server;

import com.yufa.xz.ly.enumeration.RpcMessageType;
import com.yufa.xz.ly.enumeration.RpcResponseCode;
import com.yufa.xz.ly.factory.SingletonFactory;
import com.yufa.xz.ly.remoting.dto.RpcRequest;
import com.yufa.xz.ly.remoting.dto.RpcResponse;
import com.yufa.xz.ly.remoting.handler.RpcRequestHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author LiuYe
 * @data 2020/9/16
 */
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private final RpcRequestHandler rpcRequestHandler;

    public NettyServerHandler() {
        this.rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {

            log.info("server receive msg: [{}]", msg);
            log.info("sleep some second: [{}]", 5);
            Thread.sleep(5000);
            RpcRequest rpcRequest = (RpcRequest) msg;
            if (rpcRequest.getRpcMessageType() == RpcMessageType.HEART_BEAT) {
                log.info("receive heat beat msg form client");
                return;
            }
            Object result = rpcRequestHandler.handle(rpcRequest);
            log.info(String.format("server get result: %s", result.toString()));
            if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                RpcResponse<Object> rpcResponse = RpcResponse.success(result, rpcRequest.getRequestId());
                ctx.writeAndFlush(rpcResponse).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            } else {
                RpcResponse<Object> rpcResponse = RpcResponse.fail(RpcResponseCode.FAIL);
                ctx.writeAndFlush(rpcResponse).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                log.error("not writable now, message dropped");
            }
        }finally {
            // 如果不手动释放，有可能会导致内存泄漏
            ReferenceCountUtil.release(msg);
        }

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                log.info("idle check happen, so close the connection");
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("server catch exception");
        cause.printStackTrace();
        ctx.close();
    }
}
