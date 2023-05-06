package unipi.dataStructures.exer2;

class Node {
	
	public int id;
	public int priority;

	public Node(int id, int priority) { 

		this.id = id;
		this.priority = priority;
	}

}

class WhatAStruct {	

	public Node[] hashTable;
	public int size, constantHash, usedCells;

	// constructor
	public WhatAStruct(int mx) {
		constantHash = nextPrime(mx);
		size = nextPrime(constantHash);
		hashTable = new Node[size];
		usedCells = 0;
	}
	
	// Εισάγει ένα Node n στον πίνακα
	public boolean insert(Node n) {

		// Αν δεν δωθεί Node, ακυρώνεται η εισαγωγή και επιστρέφεται false
		if (n == null)
			return false;

		// Διατρέχει τον πίνακα κατακερματισμού ψάχνωντας κενή θέση
		int attempt = 0;
		while (attempt <= size) {

			int i = hash(n.id, attempt, this);
			// Αν βρει κενή θέση εισάγει το Node και επιστρέφει true
			if (hashTable[i] == null) {
				hashTable[i] = n;
				usedCells++;
				return true;
			}
			attempt++;
		}

		// Αν δεν βρει κενή θέση, επιστρέφει false
		return false;
	} 
	
	// Αφαιρεί το μέγιστο στοιχείο από τον πίνακα κατακερματισμού
	public Node remove() {                 
		
		// Αν ο πίνακας είναι κενός επιστρέφει null
		if (usedCells == 0)
			return null;

		// Μετατρέπει τον πίνακα κατακερματισμού σε σωρό, το μέγιστο στοιχείο είναι στην ρίζα του δέντρου (θέση 0)
		Node[] heapified = heapify(this, false);
		int id = heapified[0].id, attempt = 0;

		// Αναζητεί την ρίζα του σωρού στον πίνακα κατακερματισμού, όταν το βρει το θέτει null
		while (true) {

			int i = hash(id, attempt, this);
			if (hashTable[i] != null) {
				if (hashTable[i].id == id) {
					hashTable[i] = null;
					break;
				}
			}
			attempt++;
		}
		
		// Μειώνει τον μετρητή κόμβων στον πίνακα και επιστρέφει το μέγιστο στοιχείο
		usedCells--;
		return heapified[0];

	} 
	
	// Ελέγχει την ύπαρξη ενός στοιχείου στον πίνακα κατακερματισμού
	public boolean contains(int id) {

		// Αν ο πίνακας είναι κενός προφανώς δεν περιέχεται το στοιχείο
		if (usedCells == 0)
			return false;

		// Αναζήτηση του στοιχείου στον πίνακα, αν βρεθεί επιστρέφεται true
		int attempt = 0;
		while (attempt <= size) {

			int i = hash(id, attempt, this);
			if (hashTable[i] != null) {
				if (hashTable[i].id == id) {
					return true;
				}
			}
			attempt++;
		}
		
		// Αν δεν βρεθεί επιστρέφει false
		return false;

	}
	
	// Επιστρέφει την ένωση δύο πινάκων κατακερματισμού
	public WhatAStruct union(WhatAStruct w) {

		Node[] strc1, strc2;
		
		// Αν ένας πίνακας είναι κενός, επιστρέφει τον άλλο, αλλιώς ταξινομεί και τα δύο σε φθίνουσα σειρά
		if (this.usedCells == 0)
			return w;
		else 
			strc1 = heapSort(this, true);

		if (w.usedCells == 0)
			return this;
		else
			strc2 = heapSort(w, true);

		int len1 = strc1.length, len2 = strc2.length;

		// Δημιουργεί το struct το οποίο θα έχει την ένωση των στοιχείων
		WhatAStruct	returnStruct = new WhatAStruct(len1 + len2);

		// Αντιγράφει το id και priority σε προσωρινούς πίνακες με αύξουσα σειρά ID
		int[] combID = new int[len1 + len2], combPrio = new int[len1 + len2];
		int i = 0, j = 0, index = 0, lastID = -1;
		while (i < len1 && j < len2) {

			if (strc1[i].id == lastID) {
				index--;
				combPrio[index] += strc1[i].priority;
				i++;
			}
			else if (strc2[j].id == lastID) {
				index--;
				combPrio[index] += strc2[j].priority;
				j++;
			}
			else if (strc1[i].id <= strc2[j].id) {
				combID[index] = strc1[i].id;
				combPrio[index] = strc1[i].priority;
				lastID = strc1[i].id;
				i++;
			}
			else if (strc1[i].id > strc2[j].id) {
				combID[index] = strc2[j].id;
				combPrio[index] = strc2[j].priority;
				lastID = strc2[j].id;
				j++;
			}

			index++;
		}

		// Προσθέτει υπολοιπόμενα στοιχεία του πίνακα 1 στον πίνακα κατακερματισμού
		while (i < len1) {

			if (strc1[i].id == lastID) {
				index--;
				combPrio[index] += strc1[i].priority;
			}
			else {
				combID[index] = strc1[i].id;
				combPrio[index] = strc1[i].priority;
				lastID = strc1[i].id;
			}

			index++;
			i++;
		}

		// Προσθέτει υπολοιπόμενα στοιχεία του πίνακα 2 στον πίνακα κατακερματισμού
		while (j < len2) {

			if (strc2[j].id == lastID) {
				index--;
				combPrio[index] += strc2[j].priority;
			}
			else {
				combID[index] = strc2[j].id;
				combPrio[index] = strc2[j].priority;
				lastID = strc2[j].id;
			}

			index++;
			j++;
		}

		// Εισάγει τα στοιχεία των ενδιάμεσων πινάκων στον νέο πίνακα κατακερματισμού
		for (int k = 0; k < index; k++) {
			returnStruct.insert(new Node(combID[k], combPrio[k]));
		}

		// Επιστρέφει το αντικείμενο
		return returnStruct;
	}	

	// Επιστρέφει όλα τα στοιχεία εκτός αυτά μόνο στο w
	public WhatAStruct diff(WhatAStruct w) {

		Node[] strc1, strc2;

		// Αν ένα struct είναι κενό, επιστρέφει το άλλο, αλλιώς ταξινομεί και τα δύο σε φθίνουσα σειρά
		if (this.usedCells == 0)
			return new WhatAStruct(this.size + w.size);
		else 
			strc1 = heapSort(this, true);

		if (w.usedCells == 0)
			return this;
		else
			strc2 = heapSort(w, true);
	
		int len1 = strc1.length, len2 = strc2.length;

		// Δημιουργεί το struct το οποίο θα επιστραφεί
		WhatAStruct	returnStruct = new WhatAStruct(len1 + len2);

		// Αντιγράφει τα id και priority σε ενδιάμεσους πίνακες βάση των κανόνων που έχουν δωθεί
		int[] combID = new int[len1 + len2], combPrio = new int[len1 + len2];
		int i = 0, j = 0, index = 0, lastID = -1;
		while (i < len1 && j < len2) {

			if (strc1[i].id == lastID) {
				index--;
				combPrio[index] += strc1[i].priority;
				i++;
			}
			else if (strc2[j].id == lastID) {
				index--;
				combPrio[index] = (combPrio[index] - strc2[j].priority >= 0) ? (combPrio[index] - strc2[j].priority) : (strc2[j].priority - combPrio[index]);
				j++;
			}
			else if (strc1[i].id <= strc2[j].id) {
				combID[index] = strc1[i].id;
				combPrio[index] += strc1[i].priority;
				lastID = strc1[i].id;
				i++;
			}
			else if (strc1[i].id > strc2[j].id) {
				index--;
				j++;
			}

			index++;
		}

		// Προσθέτει υπολοιπόμενα στοιχεία του πίνακα 1 στον πίνακα κατακερματισμού
		while (i < len1) {

			if (strc1[i].id == lastID) {
				index--;
				combPrio[index] += strc1[i].priority;
			}
			else {
				combID[index] = strc1[i].id;
				combPrio[index] += strc1[i].priority;
				lastID = strc1[i].id;
			}

			index++;
			i++;
		}

		// Προσθέτει υπολοιπόμενα στοιχεία του πίνακα 2 στον πίνακα κατακερματισμού
		while (j < len2) {

			if (strc2[j].id == lastID) {
				combPrio[index-1] = (combPrio[index-1] - strc2[j].priority >= 0) ? (combPrio[index-1] - strc2[j].priority) : (strc2[j].priority - combPrio[index-1]);
			}

			j++;
		}

		// Εισάγει τα στοιχεία των ενδιάμεσων πίνακων στο αντικείμενο
		for (int k = 0; k < index; k++) {
			returnStruct.insert(new Node(combID[k], combPrio[k]));
		}

		// Επιστρέφει το αντικείμενο
		return returnStruct;
	}

	// Επιστρέφει αντικείμενο με τους k μεγαλύτερους κόμβους του this
	public WhatAStruct kbest(int k) {
		
		// Αν ο πίνακας είναι κενός ή ζητείται να ταξινομηθούν 0, επιστρέφει κενή αντικείμενο
		WhatAStruct returnStruct = new WhatAStruct(k);
		if (this.usedCells == 0 || k == 0)
			return returnStruct;
		
		// Αν ζητούνται στοιχεία περισσότερα από αυτά που έχει ο πίνακας, επιστέφει όλη το αντικείμενο
		if (k >= usedCells)
			return this;

		// Ταξινομεί τα στοιχεία σε αύξουσα σειρά
		Node[] sorted = heapSort(this, false);
		
		// Είσαγει στοιχεία από τον ταξινομημένο πίνακα σε φθίνουσα σειρά
		for (int i = 0; i < k ; i++) {
			returnStruct.insert(sorted[sorted.length-1-i]);
		}

		return returnStruct;
	}	


	// Βρίσκει τον αμέσως επόμενο πρώτο αριθμό μετά του num
	private static int nextPrime(int num) {

		if (num == 0)
			return 2;
		// Βρίσκει τον επόμενο πρώτο αριθμό
		int prime = (num % 2 == 0) ? (num + 1) : (num + 2);
		boolean isPrime = false;

		while (!isPrime) {
			// Δεν ελέγχει για πολλαπλάσια του 2
			isPrime = true;

			// Ελέγχει αν διαιρείται με j για j από 2 μέχρι ρίζα prime			
			for (int j = 2; j*j <= prime; j++) {
				if (prime % j == 0) {
					isPrime = false;
					prime += 2;	
					break;
				} 
			}
		}
		return prime;
	}

	// Κατακερματίζει ένα στοιχείο
	public static int hash(int key, int attempt, WhatAStruct struct) {

		// Συνάρτηση hash της μορφής (h1(k) + i*h2(k)) % size, h2(k) = c - k % c
		int size = struct.size, constantHash = struct.constantHash;

		int h1 = key % size;
		int h2 = constantHash - (key % constantHash);
		return (h1 + attempt*h2) % size;
	}

	// Μεταφέρει στοιχεία ένος οποιοδήποτε πίνακα ώστε να μπορεί να μετατραπεί σε σωρό
	public static Node[] heapify(WhatAStruct struct, boolean compareByID) {
		
		Node[] hashTable = struct.hashTable; // Για readability
		Node[] heapified = new Node[struct.usedCells];
		
		// Φτιάχνει ένα καινούργιο πίνακα που δεν έχει κενά
		int cnt = 0;
		for (int i = 0; i < hashTable.length; i++) {
			int hash = hash(i,0, struct);
			if (hashTable[hash] != null) {
				heapified[cnt] = hashTable[hash];
				cnt++;
			}
		}

		// Εκτελεί την μετατροπή
		for (int i = (heapified.length-1)/2; i >= 0; i--) {
			trickleDown(i, heapified, heapified.length, compareByID);
		}

		return heapified;

	}

	// Τοποθετεί ένα στοιχείο Node[index] στην κατάλληλη θέση σε ένα σωρό βάση του κλειδιού της
	private static void trickleDown(int index, Node[] array, int size, boolean compareByID) {

		Node startNode = array[index];

		// Όσο το id/priority ενός Node είναι μικρότερο των παιδιών του, το κατεβάζει στον σωρό
		// Στοίχιση βάση ID
		if (compareByID) {

			// Γίνεται έλεγχος αν ο κόμβος έχει προσβάσιμα παιδιά
			while (index < size/2) {

				int leftChildIndex = 2*index+1, rightChildIndex = leftChildIndex+1;

				int larger = leftChildIndex;
				if (rightChildIndex < size) {
					if (array[rightChildIndex].id > array[leftChildIndex].id)
						larger = rightChildIndex;
				}

				if (startNode.id >= array[larger].id)
					break;

				array[index] = array[larger];
				index = larger;
			}

		} else { // Στοίχιση βάση priority
				
			// Γίνεται έλεγχος αν ο κόμβος έχει προσβάσιμα παιδιά
			while (index < size/2) {

				int leftChildIndex = 2*index+1, rightChildIndex = leftChildIndex+1;

				int larger = leftChildIndex;
				if (rightChildIndex < size) {
					if (array[rightChildIndex].priority > array[leftChildIndex].priority)
						larger = rightChildIndex;
				}

				if (startNode.priority >= array[larger].priority)
					break;

				array[index] = array[larger];
				index = larger;
			}
		}

		array[index] = startNode;
	}

	// Μετατρέπει έναν σωρό σε ταξινομημένο πίνακα σε αύξουσα σειρά 
	public static Node[] heapSort(WhatAStruct struct, boolean compareByID) {

		// Μετατρέπει τον πίνακα κατακερματισμού σε σωρό
		Node[] heapified = heapify(struct, compareByID);

		// Από τον τελευταίο κόμβο μέχρι τον πρώτο
		int index = heapified.length-1;
		while (index > 0) {
			// Αντιμεταθέτει την ρίζα με το index-οστό στοιχείο του πίνακα
			Node tempNode = heapified[index];
			heapified[index] = heapified[0];
			heapified[0] = tempNode;

			// Τοποθετεί κατάλληλα το index-οστό στοιχείο στον σωρό
			trickleDown(0, heapified, index, compareByID);
			index--;
		}

		return heapified;
	}

}