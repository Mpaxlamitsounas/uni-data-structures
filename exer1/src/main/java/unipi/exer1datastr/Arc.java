package unipi.exer1datastr;

class Arc {
	public int from;
	public int to;
	public int weight;

	public Arc(int from, int to, int weight) {
		this.from = from;
		this.to = to;
		this.weight = weight;
	}

	public Arc copy() {
		return new Arc(this.from, this.to, this.weight);
	}
}