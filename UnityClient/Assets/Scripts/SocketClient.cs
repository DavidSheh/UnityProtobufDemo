using UnityEngine;
using System.Net;
using System.Net.Sockets;
using System.Threading;

namespace Hotcast.VR.Net
{
    public class SocketClient
    {
        private static Socket clientSocket;
        //是否已连接的标识
        public bool IsConnected = false;

        public SocketClient()
        {
            clientSocket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
        }

        /// <summary>
        /// 连接指定IP和端口的服务器
        /// </summary>
        /// <param name="ip"></param>
        /// <param name="port"></param>
        public void Connect(string ip, int port)
        {
            IPAddress mIp = IPAddress.Parse(ip);
            IPEndPoint ip_end_point = new IPEndPoint(mIp, port);

            try
            {
                clientSocket.Connect(ip_end_point);
                IsConnected = true;
                Debug.Log("连接服务器成功");

                Thread threadReceive = new Thread(ReceiveMsg);
                threadReceive.IsBackground = true;
                threadReceive.Start();
            }
            catch
            {
                IsConnected = false;
                Debug.Log("连接服务器失败");
                return;
            }
        }

        public void Disconnect()
        {
            if (null != clientSocket && clientSocket.Connected)
            {
                clientSocket.Shutdown(SocketShutdown.Both);
                clientSocket.Close();
            }
        }

        /// <summary>
        /// 发送数据给服务器
        /// </summary>
        public void Send(Google.Protobuf.IMessage msg)
        {
            if (IsConnected == false)
            {
                Debug.LogError("服务器未连接！");
                return;
            }

            if(null == msg)
            {
                Debug.LogError("消息不能为空！");
                return;
            }
            
            try
            {
                clientSocket.Send(ProtobufEncoding.Encode(msg));
            }
            catch
            {
                IsConnected = false;
                clientSocket.Shutdown(SocketShutdown.Both);
                clientSocket.Close();
            }
        }

        private void ReceiveMsg()
        {
            byte[] buffer = new byte[1024];
            int len = 0;
            while (true)
            {
                len = clientSocket.Receive(buffer);
                if(len <= 0)
                {
                    continue;
                }

                var result = ProtobufEncoding.Decode(buffer) as Message;
                Debug.Log(result.MsgType + ", " + result.Agree + ", " + result.Msg);

                // TODO: 消息分发
            }
        }
    }
}