cmake_minimum_required(VERSION 3.4.1)

add_library(
             ArithHelper

             SHARED

             src/main/cpp/ArithHelper.c)

find_library(
              log-lib


              log )

target_link_libraries(
                       ArithHelper

                       ${log-lib} )