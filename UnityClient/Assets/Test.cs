using UnityEngine;
using Hotcast.VR.Net;

public class Test : MonoBehaviour {
    public string ip = "127.0.0.1";
    public int port = 8082;

    private SocketClient mSocket;

    // Use this for initialization
    void Start()
    {
        mSocket = new SocketClient();
        mSocket.Connect(ip, port);

        Message msg = new Message
        {
            MsgType = 0,
            Agree = true,
            Msg = Google.Protobuf.ByteString.CopyFromUtf8("123")
        };
        mSocket.Send(msg);
    }

    void OnApplicationQuit()
    {
        if (null != mSocket)
        {
            mSocket.Disconnect();
        }
    }
}
