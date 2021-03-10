TEMPLATE = subdirs

SUBDIRS = \
    lib \
    tests/QtDwpModelTest

# Make sure that lib is built before the tests.
CONFIG += ordered
