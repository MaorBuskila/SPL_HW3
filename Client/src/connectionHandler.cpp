
#include "../include/connectionHandler.h"

using boost::asio::ip::tcp;

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;

ConnectionHandler::ConnectionHandler(string host, short port) : host_(host), port_(port), io_service_(),
                                                                socket_(io_service_) {}

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
    catch (std::exception &e) {
        std::cerr << "Connection failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::getBytes(char bytes[], unsigned int bytesToRead) {
    size_t tmp = 0;
    boost::system::error_code error;
    try {
        while (!error && bytesToRead > tmp) {
            tmp += socket_.read_some(boost::asio::buffer(bytes + tmp, bytesToRead - tmp), error);
        }
        if (error)
            throw boost::system::system_error(error);
    } catch (std::exception &e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::sendBytes(const char bytes[], int bytesToWrite) {
    int tmp = 0;
    boost::system::error_code error;

    try {
        while (!error && bytesToWrite > tmp) {
            tmp += socket_.write_some(boost::asio::buffer(bytes + tmp, bytesToWrite - tmp), error);
        }
        if (error)
            throw boost::system::system_error(error);
    } catch (std::exception &e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::getLine(std::string &line) {

    return getFrameAscii(line, ';');
}

bool ConnectionHandler::sendLine(std::string &line) {
    cout << "sennding " << line << endl;
    return sendFrameAscii(line, ';');
}

bool ConnectionHandler::getFrameAscii(std::string &frame, char delimiter) {


    char ch;
    short opcode = -1;
    int opcodeCounter = 0;
    int msgOpcodeCounter = 0;
    short msgOpcode = -1;
    char *Name;
    char *opcodeFrom;
    int counterForSpace = 0;
    bool checkStart = false;
    vector<char> opcodeBytes;
    vector<char> msgOpcodeBytes;
    int ackCounter = 2;
    string msg = "";

    try {
        do {
            if (opcode == -1) {
                getBytes(&ch, 1);
                opcodeBytes.push_back(ch);
                opcodeCounter++;
                if (opcodeCounter == 2) {
                    opcode = opcodeFinder(opcodeBytes);
                }
            } else {
                if (opcode == 9) //notification
                {
                    if (!checkStart) {
                        msg += "NOTIFICATION ";
                        getBytes(&ch, 1);
                        int pmOrPublic = ch - '0';
                        if (pmOrPublic == 0) {
                            msg += "PM ";
                        } else {
                            msg += "Public ";
                        }
                        checkStart = true;
                    } else {
                        getBytes(&ch, 1);
                        if (ch != '\0') {
                            Name = Name + ch;
                        } else {
                            msg = msg + Name + " ";
                        }
                    }
                }
                //1001; -REGISTER
                //1002; LOGIN

                if (opcode == 10){//ACK
                    getBytes(&ch, 1);
//                    if (ackCounter == 0) {
                        if (msgOpcode == -1) {
                            msgOpcodeBytes.push_back(ch);
                            msgOpcodeCounter++;
                            if (msgOpcodeCounter == 2) {
                                msgOpcode = opcodeFinder(msgOpcodeBytes);
                                msg = "ACK "  + to_string(msgOpcode);
                            }
                        }
                        else {
                            if (msgOpcode == 3){ //LOGOUT
                                cout  << "CLOSING" << endl;
                                this->close();
                            }
                            if (msgOpcode == 4){ //FOLLOW
                                //getBytes(&ch, 1);
                                if (ch == '\0')
                                    msg += &" " [ch];
                            }
                            if (msgOpcode == 7 || msgOpcode == 8){ //LOGSTAT
                                counterForSpace++;
                                getBytes(&ch, 1);
                                Name = Name + ch;
                                if (counterForSpace == 2) {
                                    msg += " ";
                                    counterForSpace = 0;
                                }
                            }
                        }
//                    } else {
//                        if (ackCounter == 2) msg = msg + "ACK ";
//                        ackCounter--;
//                    }
                }
                if (opcode == 11)//ERROR
                {
                    if (msgOpcode == -1) {
                        getBytes(&ch, 1);
                        msgOpcodeBytes.push_back(ch);
                        msgOpcodeCounter++;
                        if (msgOpcodeCounter == 2) {
                            msgOpcode = opcodeFinder(msgOpcodeBytes);
                            msg = msg + "ERROR " + to_string(msgOpcode);
                            frame = msg;
                            return true;
                        }
                    }
                }
            }
            //getBytes(&ch, 1);
            //frame.append(1, ch);
        } while (delimiter != ch);

    } catch (std::exception &e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    frame = msg;
    return true;
}

short ConnectionHandler::opcodeFinder(vector<char> &bytesVec) {
    short temp = 10 * (bytesVec[0] - '0') + bytesVec[1] - '0';
    return temp;
}
//todo: delete all above


vector<string> ConnectionHandler::split(string &frame,char delimiter) {

    vector<string> arguments;
    size_t pos = 0;
    string token;
    while ((pos = frame.find(delimiter)) != std::string::npos) {
        token = frame.substr(0, pos);
        frame.erase(0, pos + 1);
        arguments.push_back(token);
    }
    arguments.push_back(frame);
    return arguments;


}

void ConnectionHandler::shortToBytes(short num, char *bytesArr) {
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}

short ConnectionHandler::bytesToShort(char *bytesArr) {
    short result = (short) ((bytesArr[0] & 0xff) << 8);
    result += (short) (bytesArr[1] & 0xff);
    return result;
}


bool ConnectionHandler::sendFrameAscii(const std::string &frame, char delimiter) {

    vector<char> charVec;
    char opByteArray[2];
    string line = frame;
    vector<string> args = split(line,' ');
    string type = args[0];
    if (type == "REGISTER") {
        short OPCODE = 1;
        shortToBytes(OPCODE, opByteArray);
        charVec.push_back(*opByteArray);
        charVec.push_back(*(opByteArray + 1));
        string username = args[1];
        for (char c: username) {
            charVec.push_back(c);
//            wantedLength++;
        }
        charVec.push_back('\0');
//        wantedLength++;
        string password = args[2];
        for (char c: password) {
            charVec.push_back(c);
//            wantedLength++;
        }
        charVec.push_back('\0');
//        wantedLength++;
        string birtday = args[3];
        for (char c: birtday) {
            charVec.push_back(c);
//            wantedLength++;
        }
        charVec.push_back('\0');
//        wantedLength++;

    }
    if (type == "LOGIN") {
        short OPCODE = 2;
        shortToBytes(OPCODE, opByteArray);
        charVec.push_back(*opByteArray);
        charVec.push_back(*(opByteArray + 1));
//        wantedLength += 2;
        string username = args[1];
        for (char c: username) {
            charVec.push_back(c);
//            wantedLength++;
        }
        charVec.push_back('\0');
//        wantedLength++;
        string password = args[2];
        for (char c: password) {
            charVec.push_back(c);
//            wantedLength++;
        }
        charVec.push_back('\0');
//        wantedLength++;
        string captcha = args[3];
        charVec.push_back(captcha[0]);
//        wantedLength++;
        charVec.push_back('\0');
//        wantedLength++;


    }
    if (type == "LOGOUT") {
        short OPCODE = 3;
        shortToBytes(OPCODE, opByteArray);
        charVec.push_back(*opByteArray);
        charVec.push_back(*(opByteArray + 1));

    }
    if (type == "FOLLOW") {
        short OPCODE = 4;
        shortToBytes(OPCODE, opByteArray);
        charVec.push_back(*opByteArray);
        charVec.push_back(*(opByteArray + 1));
        string followOrUn = args[1];
        charVec.push_back(followOrUn[0]);
        string username = args[2];
        for (char c: username) {
            charVec.push_back(c);
        }
        charVec.push_back('\0');


    }
    if (type == "POST") {
        short OPCODE = 5;
        shortToBytes(OPCODE, opByteArray);
        charVec.push_back(*opByteArray);
        charVec.push_back(*(opByteArray + 1));
        string content = args[1];
        for (char c: content) {
            charVec.push_back(c);
        }
        charVec.push_back('\0');


    }
    if (type == "PM") {
        short OPCODE = 6;
        shortToBytes(OPCODE, opByteArray);
        charVec.push_back(*opByteArray);
        charVec.push_back(*(opByteArray + 1));
    }

    if(type=="LOGSTAT")
    {
        short OPCODE=7;
        shortToBytes(OPCODE,opByteArray);
        charVec.push_back(*opByteArray);
        charVec.push_back(*(opByteArray+1));
    }
    if(type=="STATS")
    {
        short OPCODE=8;
        shortToBytes(OPCODE,opByteArray);
        charVec.push_back(*opByteArray);
        charVec.push_back(*(opByteArray+1));
        //adir adir adir adir
        for(int i=1;i<args.size();i++)
        {
            for(char ch : args[i])
            {
                charVec.push_back(ch);
            }
            if(i!=args.size()-1)
                charVec.push_back('|');
        }
        charVec.push_back('\0');

    }


    if (type == "BLOCK") {
        short OPCODE = 9;
        shortToBytes(OPCODE, opByteArray);
        charVec.push_back(*opByteArray);
        charVec.push_back(*(opByteArray + 1));
        string username = args[1];
        for (char c: username) {
            charVec.push_back(c);
//            wantedLength++;
        }
        charVec.push_back('\0');
    }

    int len = 0;
    char byteArray[charVec.size()];
    for (char &c: charVec) {
        byteArray[len++] = c;
    }

    bool result = sendBytes(byteArray, charVec.size());
    if (!result) return false;
    return sendBytes(&delimiter, 1);
}

// Close down the connection properly.
void ConnectionHandler::close() {
    try {

        socket_.close();

    } catch (...) {
        std::cout << "closing failed: connection already closed" << std::endl;
    }
}