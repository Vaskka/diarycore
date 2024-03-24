package com.vaskka.diary.core.model.web;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ModifiedHttpServletRequest extends HttpServletRequestWrapper {
    private final byte[] modifiedRequestBodyBytes;

    public ModifiedHttpServletRequest(HttpServletRequest request, String modifiedRequestBody) {
        super(request);
        this.modifiedRequestBodyBytes = modifiedRequestBody.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new BytesServletInputStreamImpl(new ByteArrayInputStream(modifiedRequestBodyBytes));
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    private static class BytesServletInputStreamImpl extends ServletInputStream {

        private final ByteArrayInputStream inputStream;

        public BytesServletInputStreamImpl(ByteArrayInputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public boolean isFinished() {
            return inputStream.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true; // ByteArrayInputStream总是准备好的
        }

        @Override
        public void setReadListener(ReadListener listener) {
            throw new UnsupportedOperationException("Async reading not supported");
        }

        @Override
        public int read() throws IOException {
            return inputStream.read();
        }
    }
}
