#include <stdlib.h>
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

    //From here we will see the rest of the ehco client implementation:
    //create 2 Threads for writing and reading

    std::thread write_thread([connectionHandler] {
        while (1) {
            const short bufsize = 1024;
            char buf[bufsize];
            std::cin.getline(buf, bufsize);
            std::string line(buf);
//            string line="REGISTER adirelad 101 11-11-1998";
            bool send  = connectionHandler->sendLine(line); //appends '\n' to the message. Therefor we send len+1 bytes.
            int len = line.length();
            std::cout << "Sent " << len + 1 << " bytes to server" << std::endl;
            if (!send) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
        }
    });

    std::thread read_thread([connectionHandler] {
        while (1) {
            std::string answer = "";
            std::cout << "READ" << std::endl;
            // Get back an answer: by using the expected number of bytes (len bytes + newline delimiter)
            // We could also use: connectionHandler.getline(answer) and then get the answer without the newline char at the end
            if (!connectionHandler->getLine(answer)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
            cout<< answer <<endl;

            int len = answer.length();
            // A C string must end with a 0 char delimiter.  When we filled the answer buffer from the socket
            // we filled up to the \n char - we must makefile sure now that a 0 char is also present. So we truncate last character.
//            answer.resize(len );
            std::cout << "Reply: " << answer << " " << len << " bytes " << std::endl << std::endl;
            if (answer == "ACK 3") {
                std::cout << "Exiting...\n" << std::endl;
                break;
            }
        }
    });
    read_thread.join();
//    while (1) {
//        const short bufsize = 1024;
//        char buf[bufsize];
//        std::cin.getline(buf, bufsize);
//        std::string line(buf);
//        int len=line.length();
//        if (!connectionHandler.sendLine(line)) {
//            std::cout << "Disconnected. Exiting...\n" << std::endl;
//            break;
//        }



    // We can use one of three options to read data from the server:
    // 1. Read a fixed number of characters
    // 2. Read a line (up to the newline character using the getline() buffered reader
    // 3. Read up to the null character
//        std::string answer;
//        // Get back an answer: by using the expected number of bytes (len bytes + newline delimiter)
//        // We could also use: connectionHandler.getline(answer) and then get the answer without the newline char at the end
//        if (!connectionHandler.getLine(answer)) {
//            std::cout << "Disconnected. Exiting...\n" << std::endl;
//            break;
//        }
//
//        len=answer.length();
//        // A C string must end with a 0 char delimiter.  When we filled the answer buffer from the socket
//        // we filled up to the \n char - we must makefile sure now that a 0 char is also present. So we truncate last character.
//        answer.resize(len-1);
//        std::cout << "Reply: " << answer << " " << len << " bytes " << std::endl << std::endl;
//        if (answer == "bye") {
//            std::cout << "Exiting...\n" << std::endl;
//            break;
//        }
//    }
    return 0;
}
//    }
