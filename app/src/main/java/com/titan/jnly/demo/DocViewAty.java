package com.titan.jnly.demo;

import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fox.foxdoc.hanlyjiang.library.fileviewer.FileViewer;
import com.fox.foxdoc.hanlyjiang.library.fileviewer.tbs.TBSFileViewActivity;
import com.tencent.smtt.sdk.WebView;
import com.titan.jnly.R;

import java.io.File;


public class DocViewAty extends AppCompatActivity {

    private static final int RC_WRITE_STOREGE = 1;
    private static final String TAG = "TBSInit";
    private static final String TBS_ZIP_FILE_PATH = "/sdcard/app_tbs.zip";
    protected WebView tbsWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        super.setContentView(R.layout.doc_layout);
        initView();
        initWebView();
    }

    private void initWebView() {
        tbsWebView = findViewById(R.id.tbs_webView);
        tbsWebView.loadUrl("file:///android_asset/docx/test.html");
        tbsWebView.setDrawingCacheEnabled(true);
    }


    private void initView() {
        findViewById(R.id.btn_open_pdf_with_mupdf).setOnClickListener(view -> {
            String fileName = getFilePath("TestPDF.pdf");
            startMuPDFActivityWithExampleFile(fileName);
        });
        findViewById(R.id.btn_open_pdf_with_tbs).setOnClickListener((view) -> {
            openFileWithTbs(getFilePath("TestPDF.pdf"));
        });
        findViewById(R.id.btn_open_doc_with_tbs).setOnClickListener(view -> {
            String path = "/storage/emulated/0/fox_dev/docx/TestDoc.doc";
            openFileWithTbs(path);
        });
        findViewById(R.id.btn_open_ppt_with_tbs).setOnClickListener(view -> {
            openFileWithTbs(getFilePath("TestPPT.ppt"));
        });
        findViewById(R.id.btn_open_excel_with_tbs).setOnClickListener(view -> {
           /* String path = Config.APP_PATH.concat(File.separator).concat("docx").concat(File.separator).concat("xlsx.xlsx");
            openFileWithTbs(path);*/
            String path = "/storage/emulated/0/fox_dev/docx/TestDoc.doc";
            openFileWithTbs(path);
        });
    }

    @NonNull
    private String getFilePath(String fileName) {
        //return new File(FILE_DIR + fileName).getAbsolutePath();
        return "";
    }

    private void openFileWithTbs(String filePath) {
        Log.d(TAG, "Open File: " + filePath);
        TBSFileViewActivity.viewFile(this, filePath);
    }

    public void startMuPDFActivityWithExampleFile(String fileName) {
        File file = new File(fileName);
        if (!file.isFile()) {
            Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        Uri uri = Uri.fromFile(file);
        FileViewer.startMuPDFActivityByUri(this, uri);
    }
}
