This plugin consists of JUnit tests for the Magic Lantern Studio com.wizzer.mle.studio.dwp plugin.
Currently there are five tests:

DwpReaderTest   - test for reading a Digital Workprint
DwpWriterTest   - test for writing a Digital Workprint
DwpStageTest    - test for finding a Stage in the DWP
DwpPropertyTest - test for finding a Property in the DWP
DwpMediaRefTest - test for finding a Media Reference in the DWP

To execute these tests from Eclipse, the java.library.path for the VM must be set in the Run/Debug
launch configuration:

-Djava.library.path=M:\projects\MagicLantern\Studio\plugins\com.wizzer.mle.studio.dwp\os\win32\x86