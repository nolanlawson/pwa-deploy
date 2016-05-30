package com.nolanlawson.pwadeploy;

import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import fi.iki.elonen.NanoHTTPD;

public class HttpServer extends NanoHTTPD {

  private static final Pattern EXTENSION_PATTERN = Pattern.compile("\\.([^.]+)$");
  private static final UtilLogger LOG = new UtilLogger(HttpServer.class);

  private Context context;

  public HttpServer(Context context, int port) {
    super(port);
    this.context = context;
  }

  @Override
  public void start() throws IOException {
    super.start();
    LOG.d("start()");

  }

  private Response serveIt(Uri uri) throws IOException {
    String path = uri.getPath();
    if (path.startsWith("/")) {
      path = path.substring(1);
    }
    // redirect to files dir
    File file = new File(context.getFilesDir(), path);
    if (!file.exists()) {
      throw new IOException("file not found: " + file.getAbsolutePath());
    } else if (file.isDirectory()) {
      throw new IOException("file is a directory: " + file.getAbsolutePath());
    }
    InputStream inputStream = new BufferedInputStream(new FileInputStream(file), 0x1000);
    String mimeType = getMimeType(uri);
    Response response = newChunkedResponse(Response.Status.OK, mimeType, inputStream);
    LOG.d("200 " + mimeType + " " + uri.toString());
    return response;
  }

  @Override
  public Response serve(IHTTPSession session) {
    Uri uri = Uri.parse(session.getUri());
    try {
      return serveIt(uri);
    } catch (IOException e) {
      try {
        // try adding "index.html"
        uri = uri.buildUpon().path(uri.getPath().replaceFirst("/*$", "/index.html")).build();
        return serveIt(uri);
      } catch (IOException e2) {
        LOG.d(e2, "error fetching file");
        LOG.d("404 " + uri.toString());
        return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "Not found");
      }
    }
  }

  private String getMimeType(Uri uri) {
    String extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
    String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    if (mimeType == null) { // some of these don't get detected correctly
      if ("js".equals(extension)) {
        return "application/javascript";
      } else if ("json".equals(extension)) {
        return "application/json";
      } else if ("webp".equals(extension)) {
        return "image/webp";
      } else if ("manifest".equals(extension) || "appcache".equals(extension)) {
        return "text/cache-manifest";
      }
    }
    return mimeType;
  }
}