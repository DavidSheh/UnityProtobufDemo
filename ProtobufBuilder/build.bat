@echo off

::协议文件路径, 最后不要跟“\”符号
set SOURCE_FOLDER=.\protos\

::C#编译器路径
set CS_COMPILER_PATH=.\tools\protoc-3.2.0-win32\bin\protoc.exe
::C#文件生成路径, 最后不要跟“\”符号
set CS_TARGET_PATH=.\net\

::Java编译器路径
set JAVA_COMPILER_PATH=.\tools\protoc-3.2.0-win32\bin\protoc.exe
::Java文件生成路径, 最后不要跟“\”符号
set JAVA_TARGET_PATH=.\java\
::删除之前创建的文件
del %CS_TARGET_PATH%\*.* /f /s /q
del %JAVA_TARGET_PATH%\*.* /f /s /q

::遍历所有文件
for /f "delims=" %%i in ('dir /b "%SOURCE_FOLDER%\*.proto"') do (
    
    echo 开始编译proto文件...
    ::生成 C# 代码
    echo %CS_COMPILER_PATH% --csharp_out=%CS_TARGET_PATH% --csharp_opt=file_extension=.cs %SOURCE_FOLDER%\%%i
    %CS_COMPILER_PATH% --csharp_out=%CS_TARGET_PATH% --csharp_opt=file_extension=.cs %SOURCE_FOLDER%\%%i
    
    ::生成 Java 代码
    echo %CS_COMPILER_PATH% --csharp_out=%CS_TARGET_PATH% --csharp_opt=file_extension=.cs %SOURCE_FOLDER%\%%i
    %JAVA_COMPILER_PATH% --java_out=%JAVA_TARGET_PATH% %SOURCE_FOLDER%\%%i
    
)

echo 协议生成完毕。

pause