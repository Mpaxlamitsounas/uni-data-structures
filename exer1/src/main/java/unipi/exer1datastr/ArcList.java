package unipi.exer1datastr;

class ArcList {

	// Σημείωση: Οι αριθμοί i, j στο διατεταγμένο ζεύγος (i,j) που μαζί με το w βάρος αποτελούν μία ακμή αναφέρονται ως άκρες

    private Node head, tail;
    private int nodeCnt;

	// constructor, ArcList είναι διπλά συνδεδεμένη λίστα
	public ArcList() { 
		this.head = new Node(null);
        this.tail = new Node(null);
        head.linkNext(tail);
        tail.linkPrev(head);
        nodeCnt = 0;
	} 

	// Επιστρέφει το μέγεθος της ArcList
	public int size() { 
		return nodeCnt;
	} 

	// Εισάγει copy του Arc που δώθηκε στην αρχή της ArcList
	public int insert(Arc arc) { 
		if (arc != null) {
            Node node = new Node(arc.copy());
            tail.prev.linkNext(node);
            node.linkPrev(tail.prev);
            tail.linkPrev(node);
            node.linkNext(tail);
            nodeCnt++;
        }
		return nodeCnt;
	} 

	// Αφαιρέι το Node αμέσως μετά το Head και επιστρέφει το arc που κρατούσε, αν δεν υπάρχει επιστρέφει null
	public Arc removeFirst() {
		if (nodeCnt == 0)
			return null;
		else {
			Arc returnArc = head.next.arc;
			head.linkNext(head.next.next);
			head.next.next.linkPrev(head);
			nodeCnt--;
			return returnArc;
		}
	}

	// Αφαιρεί τον πρώτο κόμβο που βρει που έχει τις ζητούμενες άκρες, αλλιώς επιστρέφει null
	public Arc remove(int from, int to) {
		Node returnNode = find(from, to);
		if (returnNode != null) {
			returnNode.next.linkPrev(returnNode.prev);
			returnNode.prev.linkNext(returnNode.next);
			nodeCnt--;
			return returnNode.arc.copy();
		}
		
		return null;
	}

	// Επιστρέφει αντίγραφο του πρώτου κόμβου που βρει που έχει τις ζητούμενες άκρες, αλλιώς επιστρέφει null
	public Arc arc(int from, int to) {
		Node returnNode = find(from, to);
		if (returnNode != null) {
			return returnNode.arc.copy();
		}
		
		return null;
	}

	// Αντιγράφει σε νέα ArcList όλες τις ακμές με weight μεταξύ [lb, ub] και την επιστρέφει
	public ArcList arcWeightsIn(int lb, int ub) {
		ArcList returnArcList = new ArcList();
		Node current = head;
		while (current.next != tail) {
			int arcWeight = current.arc.weight;
			if (arcWeight >= lb && arcWeight <= ub) {
				returnArcList.insert(current.arc);
			} 
		}

		return returnArcList;
	}

	// Χρησιμοποιεί την sort για να ταξινομήσει k στοιχεία της ArcList και τα επιστρέφει σε νέα ArcList
	public ArcList heaviestArcs(int k) {
		return sort(k);
	}

	// Επιστρέφει ArcList με αντίγραφα ακμών όλων υπογραφημάτων επιρροής όπου αθροιστικά κάθε υπογράφημα έχει βάρος > sum 
	public ArcList leastInfluence(int sum) {
		
		// Κάνει αντίγραφο της κύριας ArcList σε μία δευτερεύουσα
		Node current = this.head.next;
		ArcList outList = new ArcList(), tempList = new ArcList();
		while (current != this.tail) {
			tempList.insert(current.arc);
			current = current.next;
		}
		
		// Όσο η δευτερεύουσα ArcList δεν είναι κενή
		current = tempList.head.next;
		while (tempList.head.next != tempList.tail) {
			current = tempList.head.next;
			int j = current.arc.to, graphSum = 0;

			// Εισάγει τους κόμβος με άκρη to = j στην ArcList εξόδου, τους αφαιρεί από την δευτερεύουσα ArcList, και προσθέτει το βάρος της ακμής το κόμβου στην graphSum
			while (current != tempList.tail) {
				if (current.arc.to == j) {
					outList.insert(current.arc);
					current.prev.linkNext(current.next);
					current.next.linkPrev(current.prev);
					graphSum += current.arc.weight;
				}
				current = current.next;
			}

			// Αν το graphSum είναι μικρότερο από το κάτω όριο, αφαιρεί όλους τους κόμβους με άκρες to = j από την ArcList εξόδου
			if (graphSum < sum) {
				current = outList.head.next;
				while (current.arc.to == j) {
					current = current.next;
					if (current == outList.tail)
						break;
				}
				current.linkPrev(outList.head);
				outList.head.linkNext(current);
			}

		} // Ξανακάνει όλη την διαδικασία για άλλο j

		return outList;
	}

	// Επιστρέφει ArcList με αντίγραφα των ακμών που ανήκουν στα k υπογραφήματα επιρροής που έχουν αθροιστικά μεγαλύτερο βάρος
	public ArcList topInfluencers(int k) {

		if (this.nodeCnt == 0)
			return new ArcList();

		// Αντιγράφει την κύρια ArcList σε μία δευτερεύσουσα
		Node current = this.head.next;
		ArcList outList = new ArcList(), tempList = new ArcList(), temp2List = new ArcList();
		while (current != this.tail) {
			tempList.insert(current.arc);
			current = current.next;
		}

		// Κάνει την διαδικασία k φορες, ή αρκετές φορές για να διατρεχθούν όλες οι διαφορετικές άκρες ακμών to = j, όποιο έρθει πρώτα
		int count = 0;
		while (count < k && tempList.head.next != tempList.tail) {

			// Αντιγράφει την δευτερεύουσα ArcList σε μια τρίτη
			current = tempList.head.next;
			while (current != tempList.tail) {
				temp2List.insert(current.arc);
				current = current.next;
			}
			
			boolean firstRun = true;
			int maxj = 0, maxsum = 0;
			while (temp2List.head.next != temp2List.tail) {

				current = temp2List.head.next;
				int j = current.arc.to, sum = 0;
			
				if (firstRun) 
					maxj = j;

				// Διατρέχει όλη την ArcList και μετρά το βάρος του υπογραφήματος επιρροής που αποτελλείται από τις ακμές με άκρη to = j
				// Αφαιρεί τους κόμβους που μετρά
				while (current != temp2List.tail) {
					if (current.arc.to == j) {
						sum += current.arc.weight;
						current.prev.linkNext(current.next);
						current.next.linkPrev(current.prev);
					}
					current = current.next;
				}

				if (firstRun) {
					maxsum = sum;
					firstRun = false;
				}

				// Συγκρίνει τα βάρη των υπογραφημάτων
				if (sum > maxsum) {
					maxsum = sum;
					maxj = j;
				}

			} // Στο τέλος της διαδικασίας έχει βρεθεί ποιό υπογράφημα επιρροής είναι βαρύτερο, αυτό που όλες οι ακμές κατευθύνονται προς το maxj

			//Αφαιρεί από την δευτερεύουσα όλοι οι κόμβοι με άκρη to = jmax
			current = tempList.head.next;
			while(current != tempList.tail) {
				if (maxj == current.arc.to) {
					outList.insert(current.arc);
					current.prev.linkNext(current.next);
					current.next.linkPrev(current.prev);
				}
				current = current.next;
			}

			count++;
		} // Στην επόμενη επανάληψη θα γίνει ξανά η διαδικασία αλλά χωρίς πιο βαριά τα προηγουμένως μέγιστα υπογραφήματα
	
		return outList;
	}

	// Ψάχνει Arc βάσει τις άκρες του και το επιστρέφει αν το βρει, αλλιώς επιστρέφει null | Custom μέθοδος
	private Node find(int from, int to) {
		Node current = head;
		while (current.next != tail) {
			current = current.next;
			if (current.arc.to == to && current.arc.from == from) {
				Node returnNode = current;
				return returnNode;
			}
		}
		
		return null;
	} 

	// Ταξινομεί k Nodes της ArcList σε φθίνουσα σειρά και τα αποθηκεύει σε καινούργια ArcList | Custom μέθοδος
	private ArcList sort(int k) {

		// Αν η ArcList είναι κενή επιστρέφει νέα κενή ArcList
		if (nodeCnt == 0)
			return new ArcList();

		if (k > nodeCnt)
			k = nodeCnt;

		int count = 0;
		Node current = this.head.next;
		ArcList maxList = new ArcList(), tempList = new ArcList();
		while (current != this.tail) {
			tempList.insert(current.arc);
			current = current.next;
		} // Αντιγράφει την κύρια ArcList σε μία δευτερεύουσα 

		current = tempList.head.next;
		while (count < k) {
			Node maxNode = current;
			int max = current.arc.weight;

			// Διατρέχει όλη την ArcList και βρίσκει μέγιστο
			while (current != tempList.tail) {
				if (current.arc.weight > max) {
					maxNode = current;
					max = current.arc.weight;
				}
				current = current.next;
			}

			// Αφαιρεί το μέγιστο που βρήκε από την δευτερεύουσα ArcList, το προσθέτει στην ArcList εξόδου
			maxNode.prev.linkNext(maxNode.next);
			maxNode.next.linkPrev(maxNode.prev);
			tempList.nodeCnt--;
			maxList.insert(maxNode.arc);
			
			current = tempList.head.next;
			count++;

		} // Κάνει αυτή την διαδικασία k φορές

		return maxList;
	}

}