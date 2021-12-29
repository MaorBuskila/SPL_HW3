cmake_minimum_required(VERSION 3.21)
project(Client)

set(CMAKE_CXX_STANDARD 11)

find_package(Boost REQUIRED COMPONENTS system)

include_directories("C:/cygwin64/usr/include")

add_executable(Client src/echoClient.cpp src/connectionHandler.cpp include/connectionHandler.h src/writeMessage.cpp include/writeMessage.h src/readMessage.cpp include/readMessage.h)

target_link_libraries(${PROJECT_NAME} ${Boost_LIBRARIES})