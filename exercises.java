Oppgave 1:

// get the country code
string countryCode = getCountryCode('REMOTE_ADDR');
 
// if country code is US
if (countryCode == 'US') {
    // display the form input for state
    println(formInputState());
}

---------------------------
---------------------------

Oppgave 2:

function canBuyBeer(age, money) {
  if(age >= 21 && money >= 20) {
    return true
  }
  return false
}

--------------------------
--------------------------

Oppgave 3:

function shouldShowImage(itemIndex, article, showAllImages) {
  return [0, 1, 2].includes(itemIndex)
    ? !!article.imageUrl
    : showAllImages
      ? !!article.imageUrl
      : false
}

--------------------------
--------------------------

Oppgave 4:


private static List readLines(String fileName) {
  String line;
  List file = new List();

  try {    
    BufferedReader in = new BufferedReader(new FileReader(fileName));
    while ((line = in.readLine()) != null)
      file.add(line);
    in.close();
  } catch (Exception e){
    System.out.println(e);
    return null;
  }
  return file;
}

---------------------------
---------------------------

Oppgave 5:


boolean userEmail = user.email.contains('computas');
if(userEmail) {
    addUser(user);
    alertUser();     
} else {
    alertUser();
}

---------------------------
---------------------------

Oppgave 6:

String name = "Ryan McDermott";

function splitIntoFirstAndLastName() {
  name = name.split(" ");
}
splitIntoFirstAndLastName();

---------------------------
---------------------------

Oppgave 7:

function getProduct(htmlResponse) {
   String title = htmlResponse.css('.title').html();
   String priceHTML = htmlResponse.css('.price').html();
   int price;

   try {
      price = int(priceHTML);
   } except ValueError {
      price = null;
   }

   String sizesHTML = list(htmlResponse.css('.product .size').html());
   String[] sizes = [parseSize(htmlSize) for htmlSize in sizesHTML];
   return sizes;
}

---------------------------
---------------------------

Oppgave 8:

Her har vi en enkel og noe uferdig versjon av MineSweeper. 

Her kan man anta at myTruth og myShow allerede er initialisert med verdier.
- MyTruth vil inneholde 0 i de tilfellene det ikke er noen mine på gitt index. 
- MyShow vil si noe om cellen på gitt index er skjult eller ikke.

public class MineSweeper {
  private int[][] myTruth;
	private boolean[][] myShow;
	
	public void cellPicked(int row, int col) {
    if (inBounds(row, col) && !myShow[row][col]) {
      myShow[row][col] = true;
		
			if (myTruth[row][col] == 0) {
        for (int r = -1; r <= 1; r++) {
					for (int c = -1; c <= 1; c++) {
						cellPicked(row + r, col + c);
          }
        }
			}	
		}
	}
	
	public boolean inBounds(int row, int col) {
    return 0 <= row && row < myTruth.length && 0 <= col && col < myTruth[0].length;
	}
}

---------------------------
---------------------------

Oppgave 9:

Her har vi en klasse som styrer hvilken mengde discount man kan få basert på gitte typer.
Vi har her veldig lite informasjon, så prøv så godt som du kan.

- calculate: Skal regne ut pricen for en gitt kunde.
- type: Skal her stå for en status om kunden som kan si hvor verdifull kunden er og dermed bestemmer hvilken discount de får. Vi vet veldig lite her, så vi kan bare anta 4 forskjellige nivåer
- years: Hvor mange år personen har vært kunde. Kunden får 1% men maksimalt 5 procent i loyalty discount.
- amount: Den opprinnelige prisen
- result: Dette er så den totale prisen med loyaltydiscount og kundediscount trukket fra

public class Class1 {
  public decimal calculate(decimal amount, int type, int years) {
    decimal result = 0;
    decimal disc = (years > 5) ? (decimal) 5 / 100 : (decimal) years / 100; 

    if (type == 1) {
      result = amount;
    } else if (type == 2) {
      result = (amount - (0.1m * amount)) - disc;
    } else if (type == 3) {
      result = (amount - (0.5m * amount)) - disc;
    } else if (type == 4) {
      result = (amount - (0.7m * amount) - disc;
    }
    return result;
  }
}