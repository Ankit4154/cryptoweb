<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Books!</title>
</head>
<body style="font-family:Arial;font-size:20px;">
<div style="height:65px;align: center;background: #2075E5;font-family: Arial;color: white;">
	<br><b>
	<a href="" style="font-family:garamond;font-size:34px;margin:0 0 0 10px;color:white;text-decoration: none;">Books<i> Save it!</i></a></b>            	    	
</div>
<br><br>
<form method="POST" action="<%=request.getContextPath()%>/auth">
      <fieldset>
	    <legend>Log In</legend>	    
	    <table>
	    	<tr>
	    		<td><label>Email:</label></td>
        		<td>
        			<input type="text" name="email"><br>        			
        		</td>
        	</tr>
        	<tr>
        		<td><label>Password:</label></td>
        		<td>
        			<input type="password" name="password"><br>
        		</td>        
        	</tr>
        	<tr>
        		<td>&nbsp;</td>
        		<td><input type="submit" name="submitLoginForm" value="Log In"></td>
        	</tr>
        </table>
	  </fieldset>      
    </form>
	<p style="font-size: 15px">
			Request User agent : <%= request.getHeader("User-Agent") %>
			<br><br>
			Request language : <%= request.getLocale() %>
	<p>
</body>
</html>