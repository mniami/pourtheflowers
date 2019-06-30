import argparse
import glob

parser = argparse.ArgumentParser(
    description='Converts files with lineendings crlf to lf (windows to unix)')
parser.add_argument('path', help='Path to the files')
parser.add_argument('--extensions', '-e', help='Extensions')

args = parser.parse_args()

# replacement strings
WINDOWS_LINE_ENDING = b'\r\n'
UNIX_LINE_ENDING = b'\n'

# relative or absolute file path, e.g.:
print(args.extensions)
path = args.path
extensions = [path + extension for extension in args.extensions.split(',')]

for path in extensions:
    for file in glob.glob(path, recursive=True):
        print(str(file))
        try:
            with open(file, 'rb') as open_file:
                content = open_file.read()

            content = content.replace(WINDOWS_LINE_ENDING, UNIX_LINE_ENDING)

            with open(file, 'wb') as open_file:
                open_file.write(content)
        except:
            continue
