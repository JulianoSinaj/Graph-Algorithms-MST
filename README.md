# Minimum Spanning Tree and Connected Components
This project is part of the Algorithms and Data Structures course at the University of Camerino. It focuses on implementing graph algorithms using adjacency matrices and disjoint sets. The key topics covered include:

Graph Representation: Using an adjacency matrix for undirected graphs.
Disjoint Sets: Implementing Union-Find with path compression and union by rank.
Connected Components: Computing connected components in an undirected graph.
Minimum Spanning Tree (MST): Implementing Kruskal's Algorithm for MST computation.

Features
Graph Representation:
Implements an undirected graph using an adjacency matrix.
Supports weighted edges.
Dynamically resizes the adjacency matrix when adding/removing nodes.

Disjoint Sets (Union-Find):
Implements path compression for efficient findSet().
Uses union by rank to optimize union() operations.

Connected Components Calculation:
Uses disjoint sets to efficiently determine the connected components of an undirected graph.

Kruskal’s Minimum Spanning Tree Algorithm:
Sorts edges by weight.
Uses Union-Find to avoid cycles.
Constructs an MST with O(E log V) complexity.

📖 References
Introduction to Algorithms (Cormen, Leiserson, Rivest, Stein) – Chapters 21 & 23.
Algorithms, Part 2 (Princeton University, Coursera).

👨‍💻 Author
Developed by YOUR NAME
📧 Email: juliano.sinaj@studenti.unicam.it
