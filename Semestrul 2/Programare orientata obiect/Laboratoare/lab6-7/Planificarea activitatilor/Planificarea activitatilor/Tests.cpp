#include "Tests.h"
#include <cstdlib>
#include <sstream>

void runDomainTests()
{
	Activity a{ 1, "titlu", "descriere", "tip", 2 };
    std::ostringstream out;
    out << a;
    std::string expected = "Activitatea: 1 | Titlu: titlu | Descriere: descriere | Tip: tip | Durata: 2";
    assert(out.str() == expected);

	assert(a.getId() == 1);
	assert(a.getTitle() == "titlu");
	assert(a.getDescription() == "descriere");
	assert(a.getType() == "tip");
	assert(a.getDuration() == 2);

	a.setTitle("altTitlu");
	assert(a.getTitle() == "altTitlu");

	a.setDescription("altaDescriere");
	assert(a.getDescription() == "altaDescriere");

	a.setType("altTip");
	assert(a.getType() == "altTip");

	a.setDuration(4);
	assert(a.getDuration() == 4);

}

void runRepositoryTests()
{
    Repository repo;

    Activity a{ 1, "titlu", "descriere", "tip", 2 };
    assert(repo.store(a) == true);
    assert(repo.getAll().size() == 1);

    Activity a2{ 2, "titlu2", "descriere2", "tip2", 3 };
    assert(repo.store(a2) == true);
    assert(repo.getAll().size() == 2);

    bool exception = false;
    try {
        repo.store(a);
    }
    catch (const Exception&) {
        exception = true;
    }
    assert(exception);
    assert(repo.getAll().size() == 2);

    exception = false;
    try {
        repo.find(3);
    }
    catch (const Exception&) {
        exception = true;
    }
    assert(exception);

    const Activity& found = repo.find(2);
    assert(found.getId() == a2.getId());

    assert(repo.remove(1) == true);
    assert(repo.getAll().size() == 1);

    exception = false;
    try {
        repo.find(1);
    }
    catch (const Exception&) {
        exception = true;
    }
    assert(exception);

    Activity newA{ 2,"altTitlu","altaDescriere","altTip",5 };
    assert(repo.update(newA) == true);
    assert(repo.getAll()[0].getTitle() == "altTitlu");

    assert(repo.remove(2) == true);
    assert(repo.getAll().size() == 0);

    exception = false;
    try {
        repo.remove(1);
    }
    catch (const Exception&) {
        exception = true;
    }
    assert(exception);

    exception = false;
    try {
        repo.update(newA);
    }
    catch (const Exception&) {
        exception = true;
    }
    assert(exception);
}

void runRepositoryFileTests() {
    const std::string fileName = "tests.txt";

    try { RepositoryFile repoFile(fileName); assert(false); }
    catch(Exception&){}

    {
        std::ofstream fout(fileName);
        fout << "1,Desen,Creativitate,Hobby,10\n";
        fout.close();
    }

    RepositoryFile repoFile(fileName);
    assert(repoFile.getAll().size() == 1);

    repoFile.store(Activity(2, "Pictura", "Imaginatie", "Arta", 20));
    RepositoryFile reload1(fileName);
    assert(reload1.getAll().size() == 2);
    assert(reload1.getAll()[1].getTitle() == "Pictura");

    repoFile.update(Activity(1, "Citit", "Fictiune", "Relaxare", 15));
    RepositoryFile reload2(fileName);
    const auto& updated = reload2.find(1);
    assert(updated.getTitle() == "Citit");
    assert(updated.getDescription() == "Fictiune");

    repoFile.remove(2);
    RepositoryFile reload3(fileName);
    assert(reload3.getAll().size() == 1);
    assert(reload3.getAll()[0].getId() == 1);

    std::remove(fileName.c_str());
}


void runServiceTests()
{
    Repository repository;
    ActivityList list;
    Service service{ repository ,list};

    try { service.getAll(); assert(false); }
    catch(const Exception& ) {}

    try { service.createMap(); assert(false); }
    catch (const Exception&) {}

    try { service.undo(); assert(false); }
    catch (const Exception&){}

    service.addActivity(1, "Desen", "Creativitate", "Hobby", 2);
    service.addActivity(2, "Pictura", "Imaginatie", "Arta", 3);
    service.addActivity(3, "Desen", "Programare", "Educatie", 4);
    service.addActivity(4, "Citit", "Fictiune", "Relaxare", 5);
    service.addActivity(5, "Desen", "Actiune", "Timp", 6);
    service.addActivity(6, "Film", "Actiune", "Timp", 2);
    
    service.deleteActivity(6);
    service.undo();
    assert(service.getAll().size() == 6);
    service.updateActivity(6,"Fotbal","Sport","Hobby",3);
    assert(service.getAll()[5].getTitle() == "Fotbal");
    service.undo();
    assert(service.getAll()[5].getTitle() == "Film");
    service.undo();
    assert(service.getAll().size() == 5);
    

    auto map = service.createMap();

    assert(map.size() == 3);
    assert(map["Desen"].getFrequency() == 3);
    assert(map["Pictura"].getFrequency() == 1);
    assert(map["Citit"].getFrequency() == 1);

    assert(map["Desen"].getTitle() == "Desen");
    assert(map["Pictura"].getTitle() == "Pictura");
    assert(map["Citit"].getTitle() == "Citit");

    service.deleteActivity(1);
    service.deleteActivity(2);
    service.deleteActivity(3);
    service.deleteActivity(4);
    service.deleteActivity(5);

    assert(service.addActivity(1, "Titlu", "Descriere", "Tip", 5) == 1);

    bool exception = false;
    try {
        service.addActivity(1, "Titlu", "Descriere", "Tip", 5);
    }
    catch (const Exception&) {
        exception = true;
    }
    assert(exception);

    try { service.addActivity(-1, "Titlu", "Descriere", "Tip", 5); assert(false); }
    catch (const Exception&) {}
    try { service.addActivity(2, "", "Descriere", "Tip", 5); assert(false); }
    catch (const Exception&) {}
    try { service.addActivity(3, "Titlu", "", "Tip", 5); assert(false); }
    catch (const Exception&) {}
    try { service.addActivity(4, "Titlu", "Descriere", "", 5); assert(false); }
    catch (const Exception&) {}
    try { service.addActivity(5, "Titlu", "Descriere", "Tip", 0); assert(false); }
    catch (const Exception&) {}

    const auto& activities = service.getAll();
    assert(activities.size() == 1);

    assert(service.deleteActivity(1) == 1);

    try { service.deleteActivity(-1); assert(false); }
    catch (const Exception&) {}

    try { service.deleteActivity(100); assert(false); }
    catch (const Exception&) {}

    assert(service.addActivity(10, "TitluVechi", "DescriereVeche", "TipVechi", 10) == 1);
    assert(service.updateActivity(10, "TitluNou", "DescriereNoua", "TipNou", 20) == 1);

    try { service.updateActivity(-1, "Titlu", "Descriere", "Tip", 5); assert(false); }
    catch (const Exception&) {}
    try { service.updateActivity(11, "", "Descriere", "Tip", 5); assert(false); }
    catch (const Exception&) {}
    try { service.updateActivity(12, "Titlu", "", "Tip", 5); assert(false); }
    catch (const Exception&) {}
    try { service.updateActivity(13, "Titlu", "Descriere", "", 5); assert(false); }
    catch (const Exception&) {}
    try { service.updateActivity(14, "Titlu", "Descriere", "Tip", 0); assert(false); }
    catch (const Exception&) {}
    try { service.updateActivity(20, "x", "y", "z", 10); assert(false); }
    catch (const Exception&) {}

    const Activity& found = service.findActivity(10);
    assert(found.getTitle() == "TitluNou");
    assert(found.getDescription() == "DescriereNoua");

    assert(service.deleteActivity(10) == 1);

    try { service.findActivity(10); assert(false); }
    catch (const Exception&) {}

    try { service.findActivity(-3); assert(false); }
    catch (const Exception& ){}

    service.addActivity(1, "Desen", "Creativitate", "Hobby", 2);
    service.addActivity(2, "Pictura", "Creativitate", "Hobby", 3);
    service.addActivity(3, "Invatare", "Programare", "Educatie", 4);

    auto descFiltered = service.filterBy("description", "Creativitate");
    assert(descFiltered.size() == 2);
    for (const auto& a : descFiltered)
        assert(a.getDescription() == "Creativitate");

    auto typeFiltered = service.filterBy("type", "Hobby");
    assert(typeFiltered.size() == 2);
    for (const auto& a : typeFiltered)
        assert(a.getType() == "Hobby");

    try { service.filterBy("type", "Inexistent"); assert(false); }
    catch (const Exception&) {}

    try { service.filterBy("invalid", "Creativitate"); assert(false); }
    catch (const Exception& e) { std::cout << e.what(); }

    auto sortedByTitle = service.sortBy(&Activity::compareByTitle);
    assert(sortedByTitle[0].getTitle() == "Desen");
    assert(sortedByTitle[1].getTitle() == "Invatare");
    assert(sortedByTitle[2].getTitle() == "Pictura");

    auto sortedByDesc = service.sortBy(&Activity::compareByDescription);
    assert(sortedByDesc[0].getDescription() == "Creativitate");
    assert(sortedByDesc[2].getDescription() == "Programare");

    auto sortedByTypeDur = service.sortBy(&Activity::compareByTypeAndDuration);
    assert(sortedByTypeDur[0].getType() == "Educatie");
    assert(sortedByTypeDur[1].getType() == "Hobby");
    assert(sortedByTypeDur[1].getDuration() == 2);
    assert(sortedByTypeDur[2].getDuration() == 3);

    auto original = service.sortBy(&Activity::compareByTitle);

    vector<Activity> copy;
    copy = original;

    assert(copy.size() == original.size());

    for (size_t i = 0; i < copy.size(); ++i) {
        assert(copy[i].getTitle() == original[i].getTitle());
    }

    original[0].setTitle("AltTitlu");
    assert(copy[0].getTitle() != "AltTitlu");

    try { service.addActivityInList("");assert(false); }
    catch (Exception&) { assert(true); }

    try { service.addActivityInList("Titlu"); assert(false); }
    catch (Exception&) { assert(true); }

    try { service.generateActivities(-3); assert(false); }
    catch (Exception&) { assert(true); }

    try { service.getActivitiesFromList();assert(false); }
    catch (Exception&) {assert(true);}

    try { service.exportListCSV("test.csv"); assert(false); }
    catch (Exception&) { assert(true); }

    service.addActivityInList("Desen");
    service.addActivityInList("Pictura");
    assert(service.getActivitiesFromList().size() == 2);
    service.exportListCSV("test.csv");
    service.deleteActivity(1);
    service.deleteActivity(2);
    service.deleteActivity(3);
    service.generateActivities(10);
    assert(service.getAll().size() == 10);

    service.emptyActivityList();
    try { service.getActivitiesFromList(); assert(false); }
    catch (Exception&) { assert(true); }

    DTO defaultDto;
    assert(defaultDto.getTitle() == "");
    assert(defaultDto.getFrequency() == 0);
}

void runAllTests()
{
	runDomainTests();
	runRepositoryTests();
    runRepositoryFileTests();
	runServiceTests();
	system("cls");
}