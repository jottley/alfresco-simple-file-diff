<!DOCTYPE html SYSTEM "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>${file1name} - ${file2name}</head>
    <style type="text/css">
<!--
.container {
	display: table;
}
.row {
	display:table-row;
}
.file1 {
	border: 1px solid #000;
	padding-right:5px;
	display: table-cell;
}
.file2 {
	border: 1px solid #000;
	padding-left:5px;
	display: table-cell;
}
.diff {
	background-color: #39F;
	height:18px;
}
.nodiff {
	height:18px;
}
pre {
	line-height: 18px;
}
-->
  </style>
  <body>
  <div class="container">
	  <div class="row">
		  <div class="file1">
		  	<#list file1 as line >
		  		${line}
		  	</#list>
		  </div>
		  <div class="file2">
		  	<#list file2 as line>
		  		${line}
		  	</#list>
		  </div>
	  </div>
  </div>
  </body>
</html>