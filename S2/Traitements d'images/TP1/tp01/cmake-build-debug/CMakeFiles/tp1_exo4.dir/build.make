# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.24

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:

#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:

# Disable VCS-based implicit rules.
% : %,v

# Disable VCS-based implicit rules.
% : RCS/%

# Disable VCS-based implicit rules.
% : RCS/%,v

# Disable VCS-based implicit rules.
% : SCCS/s.%

# Disable VCS-based implicit rules.
% : s.%

.SUFFIXES: .hpux_make_needs_suffix_list

# Command-line flag to silence nested $(MAKE).
$(VERBOSE)MAKESILENT = -s

#Suppress display of executed commands.
$(VERBOSE).SILENT:

# A target that is always out of date.
cmake_force:
.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /Applications/CLion.app/Contents/bin/cmake/mac/bin/cmake

# The command to remove a file.
RM = /Applications/CLion.app/Contents/bin/cmake/mac/bin/cmake -E rm -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = "/Users/mathismenaa/Documents/Travail/M1/S2/Traitements d'images/TP1/tp01"

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = "/Users/mathismenaa/Documents/Travail/M1/S2/Traitements d'images/TP1/tp01/cmake-build-debug"

# Include any dependencies generated for this target.
include CMakeFiles/tp1_exo4.dir/depend.make
# Include any dependencies generated by the compiler for this target.
include CMakeFiles/tp1_exo4.dir/compiler_depend.make

# Include the progress variables for this target.
include CMakeFiles/tp1_exo4.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/tp1_exo4.dir/flags.make

CMakeFiles/tp1_exo4.dir/src/tp1_exo4.cpp.o: CMakeFiles/tp1_exo4.dir/flags.make
CMakeFiles/tp1_exo4.dir/src/tp1_exo4.cpp.o: /Users/mathismenaa/Documents/Travail/M1/S2/Traitements\ d'images/TP1/tp01/src/tp1_exo4.cpp
CMakeFiles/tp1_exo4.dir/src/tp1_exo4.cpp.o: CMakeFiles/tp1_exo4.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir="/Users/mathismenaa/Documents/Travail/M1/S2/Traitements d'images/TP1/tp01/cmake-build-debug/CMakeFiles" --progress-num=$(CMAKE_PROGRESS_1) "Building CXX object CMakeFiles/tp1_exo4.dir/src/tp1_exo4.cpp.o"
	/Library/Developer/CommandLineTools/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -MD -MT CMakeFiles/tp1_exo4.dir/src/tp1_exo4.cpp.o -MF CMakeFiles/tp1_exo4.dir/src/tp1_exo4.cpp.o.d -o CMakeFiles/tp1_exo4.dir/src/tp1_exo4.cpp.o -c "/Users/mathismenaa/Documents/Travail/M1/S2/Traitements d'images/TP1/tp01/src/tp1_exo4.cpp"

CMakeFiles/tp1_exo4.dir/src/tp1_exo4.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/tp1_exo4.dir/src/tp1_exo4.cpp.i"
	/Library/Developer/CommandLineTools/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E "/Users/mathismenaa/Documents/Travail/M1/S2/Traitements d'images/TP1/tp01/src/tp1_exo4.cpp" > CMakeFiles/tp1_exo4.dir/src/tp1_exo4.cpp.i

CMakeFiles/tp1_exo4.dir/src/tp1_exo4.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/tp1_exo4.dir/src/tp1_exo4.cpp.s"
	/Library/Developer/CommandLineTools/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S "/Users/mathismenaa/Documents/Travail/M1/S2/Traitements d'images/TP1/tp01/src/tp1_exo4.cpp" -o CMakeFiles/tp1_exo4.dir/src/tp1_exo4.cpp.s

# Object files for target tp1_exo4
tp1_exo4_OBJECTS = \
"CMakeFiles/tp1_exo4.dir/src/tp1_exo4.cpp.o"

# External object files for target tp1_exo4
tp1_exo4_EXTERNAL_OBJECTS =

tp1_exo4: CMakeFiles/tp1_exo4.dir/src/tp1_exo4.cpp.o
tp1_exo4: CMakeFiles/tp1_exo4.dir/build.make
tp1_exo4: /opt/homebrew/lib/libopencv_gapi.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_stitching.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_alphamat.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_aruco.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_barcode.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_bgsegm.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_bioinspired.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_ccalib.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_dnn_objdetect.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_dnn_superres.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_dpm.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_face.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_freetype.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_fuzzy.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_hfs.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_img_hash.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_intensity_transform.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_line_descriptor.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_mcc.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_quality.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_rapid.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_reg.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_rgbd.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_saliency.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_sfm.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_stereo.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_structured_light.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_superres.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_surface_matching.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_tracking.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_videostab.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_viz.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_wechat_qrcode.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_xfeatures2d.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_xobjdetect.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_xphoto.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_shape.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_highgui.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_datasets.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_plot.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_text.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_ml.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_phase_unwrapping.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_optflow.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_ximgproc.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_video.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_videoio.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_imgcodecs.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_objdetect.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_calib3d.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_dnn.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_features2d.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_flann.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_photo.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_imgproc.4.7.0.dylib
tp1_exo4: /opt/homebrew/lib/libopencv_core.4.7.0.dylib
tp1_exo4: CMakeFiles/tp1_exo4.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir="/Users/mathismenaa/Documents/Travail/M1/S2/Traitements d'images/TP1/tp01/cmake-build-debug/CMakeFiles" --progress-num=$(CMAKE_PROGRESS_2) "Linking CXX executable tp1_exo4"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/tp1_exo4.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/tp1_exo4.dir/build: tp1_exo4
.PHONY : CMakeFiles/tp1_exo4.dir/build

CMakeFiles/tp1_exo4.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/tp1_exo4.dir/cmake_clean.cmake
.PHONY : CMakeFiles/tp1_exo4.dir/clean

CMakeFiles/tp1_exo4.dir/depend:
	cd "/Users/mathismenaa/Documents/Travail/M1/S2/Traitements d'images/TP1/tp01/cmake-build-debug" && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" "/Users/mathismenaa/Documents/Travail/M1/S2/Traitements d'images/TP1/tp01" "/Users/mathismenaa/Documents/Travail/M1/S2/Traitements d'images/TP1/tp01" "/Users/mathismenaa/Documents/Travail/M1/S2/Traitements d'images/TP1/tp01/cmake-build-debug" "/Users/mathismenaa/Documents/Travail/M1/S2/Traitements d'images/TP1/tp01/cmake-build-debug" "/Users/mathismenaa/Documents/Travail/M1/S2/Traitements d'images/TP1/tp01/cmake-build-debug/CMakeFiles/tp1_exo4.dir/DependInfo.cmake" --color=$(COLOR)
.PHONY : CMakeFiles/tp1_exo4.dir/depend

