package com.mo.db;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.mo.util.MyApplication;
import com.mo.view.BlogsView;

public class NetRequestString {

	public static InputStream getStringStream(String sInputString)
    {
        if (sInputString != null && !sInputString.trim().equals(""))
        {
            try
            {
                ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(sInputString.getBytes());
                return tInputStringStream;
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        return null;
    }
}
