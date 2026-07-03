#include <Teste.h>
using namespace std;

void testDomeniu() {
	Parcare p{ "eroilor",3,3,"XX-X--X--" };
	assert(p.getAdresa() == "eroilor");
	assert(p.getLinii() == 3);
	assert(p.getColoane() == 3);
	assert(p.getStare() == "XX-X--X--");
}

void testRepository() {
	ofstream fout("test.txt");
	{
		fout << "eroilor 4 4 XX-X--X--\n";
		fout << "zorilor 3 4 XX-X--X-X\n";
		fout << "gradinilor 3 3 XX-XX-X--\n";
		fout << "gheorgheni 3 2 XX-X-XX--\n";
	}
	fout.close();
	Repository repo{ "test.txt" };
	assert(repo.getAll().size() == 4);
	assert(repo.getAll()[0].getAdresa() == "gheorgheni");
	assert(repo.getAll()[1].getAdresa() == "gradinilor");
	assert(repo.getAll()[2].getAdresa() == "zorilor");
	assert(repo.getAll()[3].getAdresa() == "eroilor");
	repo.add(Parcare { "eroilor",3,3,"XX-X--X--" });
	assert(repo.getAll().size() == 4);
	repo.add(Parcare{ "buna-ziua",3,3,"XX-X--X--" });
	assert(repo.getAll().size() == 5);
	repo.update(Parcare("gheorgheni", 3, 2, "XXX"));
	assert(repo.getAll()[0].getStare() == "XXX");
}

void testService() {
	ofstream fout("test.txt");
	{
		fout << "eroilor 4 4 XX-X--X--\n";
		fout << "zorilor 3 4 XX-X--X-X\n";
		fout << "gradinilor 3 3 XX-XX-X--\n";
		fout << "gheorgheni 3 2 XX-X-XX--\n";
	}
	fout.close();
	Repository repo{ "test.txt" };
	Service serv{ repo };
	assert(serv.getAll().size() == 4);
	serv.add("buna-ziua", 2, 4, "XXX");
	assert(serv.getAll().size() == 5);
	serv.add("buna-ziua", 2, 4, "XXX");
	assert(serv.getAll().size() == 5);
	assert(serv.update("eroilor", 0, 4, "") == 1);
	assert(serv.update("eroilor", 1, 6, "") == 2);
	assert(serv.update("eroilor", 1, 3, "XXXX") == 3);
	assert(serv.update("eroilor", 1, 3, "XX0") == 4);
	assert(serv.update("eroilor", 1, 3, "XXX") == 0);
	assert(repo.getAll()[0].getStare() == "XXX");
}

void ruleazaTeste() {
	testDomeniu();
	testRepository();
	testService();
}