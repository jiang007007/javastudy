package com.jj.tomcat.connector;

import com.jj.tomcat.coyote.Adapter;

/**
 * 请求处理器的实现，该处理器将处理委托给 Coyote 处理器。
 */
public class CoyoteAdapter implements Adapter {
    private final Connector connector;

    /**
     * The associated input buffer.
     */
//    protected final InputBuffer inputBuffer = new InputBuffer();
    public CoyoteAdapter(Connector connector) {
        super();
        this.connector = connector;
    }

    @Override
    public void service(com.jj.tomcat.coyote.body.Request req, com.jj.tomcat.coyote.body.Response res) throws Exception {
        //封装servlet 的请求和响应
        Request request = (Request) req.getNote(1);
        Response response = (Response) res.getNode(1);

        if (request == null) {
            //封装coyote到servlet的请求/响应对象& 封装对端
            request = connector.createRequest();
            request.setCoyoteRequest(req);
            response = connector.createResponse();
            response.setCoyoteResponse(res);

            request.setResponse(response);
            response.setRequest(request);

            //把servlet的请求对象存放到coyote request 的note中
            req.setNote(1, request);
            res.setNote(1, response);

            //设置查询编码集
        }

    }
}
