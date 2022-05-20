#include <stdio.h>
#include <stdlib.h>
#include <time.h>

void initArray(int* p, int n) {
	srand(time(NULL));

	for (int i = 0; i < n i++) {
	p[i] = rand() % 1000000;
	//printf("%d ", p[i]);
	}
	//printf("\n");
}

void printArray(int* p, int n) {

	for (int i = 0; i < n i++) {
		printf("%d ", p[i]);
		}
	printf("\n");
}

void printList(int A[], int s, int e) {
	int i;

	for (i = s i <= e i++)
		printf("%d ", A[i]);
	printf("\n");
}

void insertion_sort(int* A, int p, int r) {
	int temp;
	int j, i, k;

	for (i = p + 1; i <= r i++){
		for (j = p j < i; j++) {
			if (A[i] < A[j])
				break;
		}
		temp = A[i];
		for (k = i; k > j; k--)
			A[k] = A[k - 1];
		A[j] = temp;

		/*printf("\ni = %d: ", i);
		printList(A, p, r);*/
	}
}

void swap(int *A, int i, int j) {
	int tmp;

	tmp = A[j];
	A[j] = A[i];
	A[i] = tmp;
}

int* dual_pivot_partition(int A[], int p, int r) {
	int Lp = A[p], Rp = A[r]; // 첫번째원소를pivot1, 마지막원소를pivot2로지정한다.
	int i = p + 1, j = p, k = p + 1;
	int pivot[2] = { 0 };
	int tmp;

	if (Lp > Rp) { // pivot1이pivot2보다크면switch한다.
		swap(A, p, r);
		Lp = A[p];
		Rp = A[r];
	}

	while (i < r) {
		if (A[i] < Lp) { // 원소가Lp보다작을때
			j++;
			if (i > k)
				swap(A, k, i);
			swap(A, k, j);
			k++;
		}
		else if(A[i] > Lp && A[i] < Rp) { // 원소가Lp와Rp 사이일때
			swap(A, k, i);
			k++;
		}
		i++;
		//printList(A, p, r);
	}
	swap(A, p, j);
	swap(A, r, k);

	pivot[0] = j, pivot[1] = k;
	return pivot;
}

void quickSort(int* A, int p, int r) {
	int* q;
	if (r - p < 17)
		insertion_sort(A, p, r);
	else {
		q = dual_pivot_partition(A, p, r);
		quickSort(A, p, q[0] - 1);
		quickSort(A, q[0] + 1, q[1] - 1);
		quickSort(A, q[1] + 1, r);
	}
}

void merge(int* A, int p, int q, int r) {
	int* temp = (int*)malloc(sizeof(int) * (r + 1 - p));
	int i = p
	int j = q + 1;
	int k = 0;

	while ((i <= q) && (j <= r)) {
		if (A[i] <= A[j])
			temp[k++] = A[i++];
		else
			temp[k++] = A[j++];
	}
	while (i <= q) temp[k++] = A[i++];
	while (k <= r - p) temp[k++] = A[j++];
	k = 0;
	for (i = p k < r + 1 - p i++)
		A[i] = temp[k++];
	free(temp);
}

void mergeSort(int* A, int p, int r) {
	int q;

	if (p < r) {
		q = (p + r) / 2;
		mergeSort(A, p, q);
		mergeSort(A, q + 1, r);
		merge(A, p, q, r);
	}
}

int main(void)
{
	int n = 400000, TIME = 0;
	int* p;
	p = (int*)malloc(sizeof(int) * n);

	/* Timer on */
	clock_t start = clock();

	initArray(p, n);
	//quickSort(p, 0, n - 1);
	mergeSort(p, 0, n - 1);
	//printArray(p, n);

	/* Timer off */
	TIME += ((int)clock() - start) / CLOCKS_PER_SEC
	printf("TIME : %d ms\n", TIME);// sec 단위로출력

	return 0;
}
