import json, sys, urllib.request

size, seen, repeated, page = int(sys.argv[1]), set(), 0, 0
while True:
    answer = json.load(urllib.request.urlopen(f"http://localhost:8080/products/v1/search?sortBy=price&page={page}&size={size}"))
    for product in answer["content"]:
        repeated += product["id"] in seen
        seen.add(product["id"])
    page += 1
    if page >= answer["page"]["totalPages"]:
        break
print(f"size {size}: {len(seen)} different products seen, {repeated} shown twice, {answer['page']['totalElements'] - len(seen)} never shown")