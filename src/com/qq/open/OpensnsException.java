package com.qq.open;







public class OpensnsException
  extends Exception
{
  private static final long serialVersionUID = 8243127099991355146L;
  




  private int code;
  





  public OpensnsException(int code, String msg)
  {
    super(msg);
    this.code = code;
  }
  





  public OpensnsException(int code, Exception ex)
  {
    super(ex);
    this.code = code;
  }
  



  public int getErrorCode()
  {
    return code;
  }
}
