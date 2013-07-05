
package com.babytree.apps.comm.exception;

import java.net.SocketException;

/**
 * 服务器异常
 */
public class ServerException extends SocketException {

    private static final long serialVersionUID = 919423109629093892L;

    public ServerException() {
        super();
    }

    public ServerException(String detailMessage) {
        super(detailMessage);
    }
}
