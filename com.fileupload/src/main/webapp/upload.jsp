<!DOCTYPE html>
<html>
<head>
    <title>File Upload</title>
</head>
<body>
    <h2>File Upload</h2>
    <form action="FileUploadServlet" method="post" enctype="multipart/form-data">
        Select File: <input type="file" name="file" /><br />
        <input type="submit" value="Upload" />
    </form>
</body>
</html>
