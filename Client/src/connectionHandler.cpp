#include "../include/connectionHandler.h"
 
using boost::asio::ip::tcp;

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;
 
ConnectionHandler::ConnectionHandler(string host, short port): host_(host), port_(port), io_service_(), socket_(io_service_){}
    
ConnectionHandler::~ConnectionHandler() {
    close();
}
 
bool ConnectionHandler::connect() {
    std::cout << "Starting connect to " 
        << host_ << ":" << port_ << std::endl;
    try {
		tcp::endpoint endpoint(boost::asio::ip::address::from_string(host_), port_); // the server endpoint
		boost::system::error_code error;
		socket_.connect(endpoint, error);
		if (error)
			throw boost::system::system_error(error);
    }
    catch (std::exception& e) {
        std::cerr << "Connection failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::getBytes(char bytes[], unsigned int bytesToRead) {
    size_t tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToRead > tmp ) {
			tmp += socket_.read_some(boost::asio::buffer(bytes+tmp, bytesToRead-tmp), error);			
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::sendBytes(const char bytes[], int bytesToWrite) {
    int tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToWrite > tmp ) {
			tmp += socket_.write_some(boost::asio::buffer(bytes + tmp, bytesToWrite - tmp), error);
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::getLine(std::string& line) {
    return getFrameAscii(line, ';');
}

bool ConnectionHandler::sendLine(std::string& line) {
    return sendFrameAscii(line, '\n');
}
 
bool ConnectionHandler::getFrameAscii(std::string& frame, char delimiter) {
    char ch;

    // Stop when we encounter the null character. 
    // Notice that the null character is not appended to the frame string.
    try {
        do {
            getBytes(&ch, 1);
            frame.append(1, ch);
        } while (delimiter != ch);
    } catch (std::exception &e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}



vector<string> ConnectionHandler::split(string& frame)
{

    string delimiter = " ";
    vector<string> arguments;
    size_t pos = 0;
    string token;
    while ((pos = frame.find(delimiter)) != std::string::npos) {
        token = frame.substr(0, pos);
        std::cout << token << std::endl;
        frame.erase(0, pos + 1);
        arguments.push_back(token);
    }
    arguments.push_back(frame);
  return arguments;


}
void ConnectionHandler::shortToBytes(short num, char* bytesArr)
{
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}


 
bool ConnectionHandler::sendFrameAscii(const std::string& frame, char delimiter) {

    int wantedLength=frame.length()-(frame.find(" ")-2);
    char byteArray[wantedLength];
    char *byteArrayPointer=&byteArray[0];

    string line=frame.substr(0,frame.find(delimiter,0)-1);
    vector<string> args=split(line);
    string type=args[0];
    enum command{
        REGISTER,LOGIN,LOGOUT,FOLLOW,STATS,LOGSTAT,UNFOLLOW,POST,PM,BLOCK
    };
    //
    //CLIENT#2< REGISTER Rick pain 12-10-1951
    if(type=="REGISTER")
    {
        short OPCODE=1;
        shortToBytes(OPCODE,byteArrayPointer);
        string username=args[1];
        int i=2;
        for(char c:username)
        {
            byteArray[i++]=c;
        }
        byteArray[i++]='\0';
        string password=args[2];
        for(char c:password)
        {
            byteArray[i++]=c;
        }
        byteArray[i++]='\0';
        string birtday=args[3];
        for(char c:birtday)
        {
            byteArray[i++]=c;
        }
        byteArray[i++]='\0';
        byteArray[i]=';';

    }
    if(type=="LOGIN")
    {
        short OPCODE=2;
        shortToBytes(OPCODE,byteArrayPointer);
        string username=args[1];
        int i=2;
        for(char c:username)
        {
            byteArray[i++]=c;
        }
        byteArray[i++]='\0';
        string password=args[2];
        for(char c:password)
        {
            byteArray[i++]=c;
        }
        byteArray[i++]='\0';
        string captcha=args[3];
       byteArray[i++]=captcha[0];
       byteArray[i]=';';

    }
    if(type=="LOGOUT")
    {
        short OPCODE=3;
        shortToBytes(OPCODE,byteArrayPointer);

    }
    if(type=="FOLLOW")
    {
        short OPCODE=4;
        shortToBytes(OPCODE,byteArrayPointer);
        int i=2;
        string followOrUn=args[1];
        byteArray[i++]=followOrUn[0];
        string username=args[2];
        for(char c:username)
        {
            byteArray[i++]=c;
        }
        byteArray[i]=';';

    }
    if(type=="POST")
    {
        short OPCODE=5;
        shortToBytes(OPCODE,byteArrayPointer);
        int i=2;
        string content=args[1];
        for(char c:content)
        {
            byteArray[i++]=c;
        }
        byteArray[i++]='\0';
        byteArray[i]=';';
    }
    if(type=="PM")
    {
    }
    if(type=="STATS")
    {}
    if(type=="LOGSTAT")
    {}


    if(type=="BLOCK")
    {}

	bool result=sendBytes(byteArray,frame.length());
	if(!result) return false;
	return sendBytes(&delimiter,1);
}
 
// Close down the connection properly.
void ConnectionHandler::close() {
    try{
        socket_.close();
    } catch (...) {
        std::cout << "closing failed: connection already closed" << std::endl;
    }
}