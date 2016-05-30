package com.nolanlawson.pwadeploy;

import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fi.iki.elonen.NanoHTTPD;

public class HttpServer extends NanoHTTPD {

  public static final int PORT = 3000;

  private static final Pattern EXTENSION_PATTERN = Pattern.compile("\\.([^\\.]+)$");
  private static final UtilLogger LOG = new UtilLogger(HttpServer.class);

  private Context context;

  public HttpServer(Context context) {
    super(PORT);
    this.context = context;
  }

  @Override
  public void start() throws IOException {
    super.start();
    LOG.d("start()");

  }

  private String getExtension(Uri uri) {
    Matcher matcher = EXTENSION_PATTERN.matcher(uri.getPath());
    if (matcher.find()) {
      return matcher.group(1);
    }
    return "html";
  }

  @Override
  public Response serve(IHTTPSession session) {
    Uri uri = Uri.parse(session.getUri());
    try {
      String path = uri.getPath();
      if ("/".equals(path)) {
        path = "/index.html";
      }
      if (path.startsWith("/")) {
        path = path.substring(1);
      }
      // redirect to assets
      InputStream inputStream = context.getAssets().open(path);

      String mimeType = MimeTypeMap.getSingleton()
          .getMimeTypeFromExtension(getExtension(uri));

      LOG.d("200 " + mimeType + " " + uri.toString());

      return newChunkedResponse(Response.Status.OK, mimeType, inputStream);
    } catch (IOException e) {
      LOG.d("404 " + uri.toString());
      return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "Not found");
    }
  }
}