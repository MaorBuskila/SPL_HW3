#include <thread>
#include "../include/connectionHandler.h"

using namespace std;

/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
int main(int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);

    ConnectionHandler *connectionHandler = new ConnectionHandler(host, port);
    if (!connectionHandler->connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    thread write_thread([connectionHandler] {
        while (1) {
            const short bufsize = 1024;
            char buf[bufsize];
            std::cin.getline(buf, bufsize);
            std::string line(buf);
            bool send  = connectionHandler->sendLine(line); //appends '\n' to the message. Therefor we send len+1 bytes.
            int len = line.length();
            std::cout << "Sent: " << line << " , " << len + 1 << " bytes to server" << std::endl;
            if (!send) {
                std::cout << "Write thread shutdown.\n" << std::endl;
                break;
            }
        }
    });

    thread read_thread([connectionHandler] {
        while (1) {
            std::string answer = "";
            // Get back an answer: by using the expected number of bytes (len bytes + newline delimiter)
            // We could also use: connectionHandler.getline(answer) and then get the answer without the newline char at the end
            if (!connectionHandler->getLine(answer)) {
                std::cout << "Read thread shutdown.\n" << std::endl;
                break;
            }
            int len = answer.length();
            std::cout << "Reply: " << answer << " " << len << " bytes " << std::endl << std::endl;
            if (answer == "ACK 3") {
                std::cout << "Exiting...\n" << std::endl;
                break;
            }
        }
    });
    read_thread.join();
    return 0;
}
//    }
