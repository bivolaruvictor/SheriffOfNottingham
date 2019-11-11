	Am creat o clasa Player care inglobeaza toate actiunile pe care le face un jucator, fie in rol de comerciant,
	fie in rol de serif(make bag, control players, arrange cards), pe care le-am suprascris in clasele ce extind Player
	(direct : basic si indirect, extinzand player greedy si bribed), pentru a le adapta cerintei si fiecarui tip in parte

	In functia main parcurg vectorul de strategii din gameLoader si creez jucatori de acel tip.
	In cele 2 for-uri simulam jocul : la fiecare sub-runda facem player(k) serif, apoi impart cartile celorlalti jucatori.
	Playerul care este serif verifica dupa aceea toti comerciantii.
	La finalul jocului, se trece prin vectorul de jucatori. Pentru fiecare element legal se trece prin StoreFrequency Map-ul fiecarui jucator,
	unde sunt stocate toate bunurile de pe taraba ( sunt doar legale deoarece bunurile ilegale sunt transformate in bonusurile
	lor in carti legale ) si se gasesc asaking si queen.
	Se dau apoi bonusurile pentru king, queen si pentru bunuri.
	La final se afiseaza scorul.

	Avand in vedere ca in anumite situatii, jucatorii de tip bribed si greedy se comporta ca un jucator basic,
	am gandit clasele BribedPlayer si GreedyPlayer ca extensii ale clasei BasicPlayer.

	Atunci cand un jucator Greedy
