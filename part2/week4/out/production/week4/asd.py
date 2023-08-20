ref = []
file = open("asd.txt")
for line in file:
    ref.append(line.strip())
file.close()

my = []
file = open("my.txt")
for line in file:
    my.append(line.strip())
file.close()

for word in ref:
    if word not in my:
        print(word)
