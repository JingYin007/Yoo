package com.mo.util;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mo.bean.Blogs;

public class SAXtoBlogs extends DefaultHandler{
	 
	  
    //��ű�������  
    private ArrayList<Blogs> blogList;  
    //����Blog����  
    private Blogs blog;  
    //�������ÿ�α������Ԫ������(�ڵ�����)  
    private String tagName;  
      
      
    public List<Blogs> getList() {  
        return blogList;  
    }  
  
  
    public void setList(ArrayList<Blogs> list) {  
        this.blogList = list;  
    }  
  
  
    public Blogs getBlog() {  
        return blog;  
    }  
  
  
    public void setBlog(Blogs blog) {  
        this.blog = blog;  
    }  
  
  
    public String getTagName() {  
        return tagName;  
    }  
  
  
    public void setTagName(String tagName) {  
        this.tagName = tagName;  
    }  
  
  
    //ֻ����һ��  ��ʼ��blogList����    
    @Override  
    public void startDocument() throws SAXException {  
    	blogList=new ArrayList<Blogs>();  
    }  
      
      
    //���ö��    ��ʼ����  
    @Override  
    public void startElement(String uri, String localName, String qName,  
            Attributes attributes) throws SAXException {  
        if(qName.equals("entry")){  
            blog=new Blogs();  
            //��ȡstudent�ڵ��ϵ�id����ֵ  
           // student.setId(Integer.parseInt(attributes.getValue(0)));  
            //��ȡstudent�ڵ��ϵ�group����ֵ  
           // student.setGroup(Integer.parseInt(attributes.getValue(1)));  
        }  
        this.tagName=qName;  
    }  
      
      
    //���ö��    
    @Override  
    public void endElement(String uri, String localName, String qName)  
            throws SAXException {  
        if(qName.equals("entry")){  
            this.blogList.add(this.blog);  
        }  
        this.tagName=null;  
    }  
      
      
    //ֻ����һ��  
    @Override  
    public void endDocument() throws SAXException {  
    }  
      
    //���ö��  
    @Override  
    public void characters(char[] ch, int start, int length)  
            throws SAXException {  
        if(this.tagName!=null){  
            String date=new String(ch,start,length);  
            if(this.tagName.equals("summary")){  
                this.blog.setSummary (date);  
                System.out.println("--summary-"+date);
            }  
            else if(this.tagName.equals("published")){  
                this.blog.setPublished(date);  
                this.blog.setUpdated(date); 
                System.out.println("--published-"+date);
            }
            else if(this.tagName.equals("id")){  
            	 System.out.println("--id-"+date);
                //this.blog.setId(date);  
            }  
            else if(this.tagName.equals("title")){  
               // this.blog.setTitle(date);  
            	 System.out.println("--title-"+date);
            } 
            else if(this.tagName.equals("updated")){  
               // this.blog.setUpdated(date.toString());  
            	 System.out.println("--updated-"+date);
            }  
            else if(this.tagName.equals("diggs")){  
                this.blog.setDiggs(Integer.parseInt(date));  
            }  
            else if(this.tagName.equals("views")){  
                this.blog.setViews(Integer.parseInt(date));  
            }  
            else if(this.tagName.equals("comments")){  
                this.blog.setComments(Integer.parseInt(date));  
            }  
            
            else if(this.tagName.equals("name")){  
                this.blog.setAuthorName(date);  
            }  
            else if(this.tagName.equals("uri")){  
                this.blog.setAuthorUri(date);  
            }  
            else if(this.tagName.equals("avatar")){  
                this.blog.setAuthorAvater(date);  
            }  
        }  
    }  
}
