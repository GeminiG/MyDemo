import networkx as nx
from matplotlib import pyplot as plt

G = nx.Graph()

nodes = [1, 2, 3, 4, 5]
G.add_nodes_from(nodes)

edges = [(1, 2), (2, 1), (3, 4), (4, 5)]
G.add_edges_from(edges)

print(G.has_edge(1, 5))