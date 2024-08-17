import java.util.*;

public class Main {
    public static ArrayList<String> routeStations = new ArrayList<>();
    static ArrayList<String> line1 = new ArrayList<>(Arrays.asList(
            "helwan", "ain helwan", "helwan university", "wadi hof", "hadayek helwan",
            "el-maasara", "tora el-asmant", "kozzika", "tora el-balad", "sakanat el-maadi", "el-maadi",
            "hadayeq el-maadi", "dar el-salam", "el-zahraa", "mar girgis", "el-malek el-saleh",
            "al-sayeda zeinab", "saad zaghloul", "sadat", "nasser", "orabi", "al shohadaa",
            "ghamra", "el-demerdash", "manshiet el-sadr", "kobri el-qobba", "hammamat el-qobba",
            "saray el-qobba", "hadayeq el-zaitoun", "helmeyet el-zaitoun", "el-matareyya",
            "ain shams", "ezbet el-nakhl", "el-marg", "new el-marg"
    ));
    static ArrayList<String> line2 = new ArrayList<>(Arrays.asList(
            "el mounib", "sakiat mekki", "omm el misryeen", "giza", "faisal",
            "cairo university", "bohooth", "dokki", "opera", "sadat", "naguib",
            "ataba", "al shohadaa", "massara", "road el-farag", "sainte teresa",
            "khalafawy", "mezallat", "koliet el-zeraa", "shobra el kheima"
    ));
    static ArrayList<String> line3 = new ArrayList<>(Arrays.asList(
            "adly mansour", "hikestep", "omar ibn al khattab", "kebaa", "hisham barakat",
            "el nozha", "el shames club", "alf maskan", "heliopolis", "haroun",
            "al ahram", "koleyet el banat", "cairo stadium", "fair zone", "abbassiya",
            "abdou pasha", "el geish", "bab el shaaria", "ataba", "nasser",
            "maspero", "zamalek", "kit kat", "sudan st.", "imbaba",
            "el bohy", "el qawmia", "ring road", "rod el farag corr"
    ));

    static ArrayList<String> line3new = new ArrayList<>(Arrays.asList(
            "kit kat", "tawfikia", "wadi el nile", "gamet el dowel", "boulak el dakrour",
            "cairo university"
    ));
    static String startStation = "";
    static String endStation = "";
    static int count;
    static String interchangeStation1, interchangeStation2;
    static int startLine;
    static int endLine;
    static ArrayList<String> startLineName;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Integer> startLines;
        List<Integer> endLines;

        while (true) {
            System.out.println("Enter start station:");
            startStation = scanner.nextLine().trim().toLowerCase();
            startLines = findLines(startStation);
            if (!startLines.isEmpty()) {
                break;
            } else {
                System.out.println("PLEASE ENTER AGAIN\n");
            }
        }

        while (true) {
            System.out.println("Enter end station:");
            endStation = scanner.nextLine().trim().toLowerCase();
            endLines = findLines(endStation);

            if (startStation.equals(endStation)) {
                System.out.println("PLEASE ENTER DIFFERENT END STATION\n ");
            } else if (!endLines.isEmpty()) {
                break;
            } else {
                System.out.println("PLEASE ENTER AGAIN\n");
            }
        }
        count = 0;

        // Check if start and end stations are on the same physical line
        Set<Integer> commonLines = new HashSet<>(startLines);
        commonLines.retainAll(endLines);

        if (!commonLines.isEmpty()) {
            int commonLine = commonLines.iterator().next();
            getNewDirection(startStation, endStation);
            System.out.println("Take Line " + commonLine + " from " + startStation.toUpperCase() + " to " + endStation.toUpperCase());
            printStations(startStation, endStation, commonLine);
            count = routeStations.size();
        } else {
            startLine = startLines.get(startLines.size() - 1);
            endLine = endLines.get(endLines.size() - 1);
            startLineName = getLineName(startLine);

            System.out.println(startLines + "       hghghghghghg     " + endLines);

            if ((startLine == 1 || endLine == 1) && (startLine == 4 || endLine == 4)) {
                handlingLines1and4(startLine, endLine, startStation, endStation);
                return;
            }
            interchangeStation1 = getInterchangeStation(startLine, endLine, startStation, startLineName);

            getNewDirection(startStation, interchangeStation1);
            System.out.println("Take Line " + startLine + " from -" + startStation.toUpperCase() + "- to -" + interchangeStation1.toUpperCase() + "- :");
            printStations(startStation, interchangeStation1, startLine);
            System.out.println("\n");

            getNewDirection(interchangeStation1, endStation);
            System.out.println("\nChange to Line " + endLine + " and from -" + interchangeStation1.toUpperCase() + "- to -" + endStation.toUpperCase() + "- :");
            printStations(interchangeStation1, endStation, endLine);
            System.out.println(" ");
            count = removeDuplicates(routeStations).size();
        }

        System.out.println("\nNumber Of Stations: " + count);
        int price = getPrice(count);
        System.out.println("Price: " + price + " EGP");
        calculateEstimatedTime(count);

        // Find all paths
        List<List<String>> allPaths = findAllPaths(startStation, endStation);
        System.out.println("\nAll possible paths from " + startStation.toUpperCase() + " to " + endStation.toUpperCase() + ":");
        int min = allPaths.get(0).size();
        int shortIndex = 0;
        for (List<String> path : allPaths) {
            count = path.size();
            if (path.size() < min) {
                min = path.size();
                shortIndex = allPaths.indexOf(path);
            }
            System.out.println(path + "\n");
        }
        System.out.println("\nShortest Route: " + allPaths.get(shortIndex));


    }

    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {
        ArrayList<T> newList = new ArrayList<T>();

        // Traverse through the first list
        for (T element : list) {
            if (!newList.contains(element)) {
                newList.add(element);
            }
        }
        return newList;
    }

    public static List<Integer> findLines(String station) {
        List<Integer> lines = new ArrayList<>();
        if (line1.contains(station)) {
            lines.add(1);
        }
        if (line2.contains(station)) {
            lines.add(2);
        }
        if (line3.contains(station)) {
            lines.add(3);
        }
        if (line3new.contains(station)) {
            lines.add(4);
        }
        return lines;  // Return all lines containing the station
    }

    public static String interchangeOfLine1(int startLine, int endLine, String startStation, ArrayList<String> startLineName) {
        String interchangeStation = "sadat";


        int startStationIndex = startLineName.indexOf(startStation);
        int shohadaIndex = startLineName.indexOf("al shohadaa");
        int interchangeStation1 = Math.abs(startStationIndex - shohadaIndex);

        int sadatIndex = startLineName.indexOf("sadat");
        int interchangeStation2 = Math.abs(startStationIndex - sadatIndex);

        if (interchangeStation1 < interchangeStation2) {
            interchangeStation = "al shohadaa";
        }

        return interchangeStation;
    }

    public static String getInterchangeStation(int startLine, int endLine, String startStation, ArrayList<String> startLineName) {
        // Interchange stations for each line
        String interchangeStations = "";
        if ((startLine == 1 || endLine == 1) && (startLine == 2 || endLine == 2)) {
            interchangeStations = interchangeOfLine1(startLine, endLine, startStation, startLineName);//1&2
        } else if ((startLine == 2 || endLine == 2) && (startLine == 4 || endLine == 4)) {
            interchangeStations = "cairo university";//2&4
        } else if ((startLine == 2 || endLine == 2) && (startLine == 3 || endLine == 3)) {
            interchangeStations = "ataba";//2&3
        } else if ((startLine == 1 || endLine == 1) && (startLine == 3 || endLine == 3)) {
            interchangeStations = "nasser";// 1&3
        } else if ((startLine == 3 || endLine == 3) && (startLine == 4 || endLine == 4)) {
            interchangeStations = "kit kat";// 3&4
        }

        return interchangeStations;
    }


    public static void handlingLines1and4(int startLine, int endLine, String startStation, String endStation) {
        interchangeStation1 = getInterchangeStation(startLine, 3, startStation, startLineName);
        interchangeStation2 = getInterchangeStation(3, endLine, interchangeStation1, startLineName);

        getNewDirection(startStation, interchangeStation1);
        System.out.println("Take Line " + startLine + " from -" + startStation.toUpperCase() + "- to -" + interchangeStation1.toUpperCase() + "- :");
        printStations(startStation, interchangeStation1, startLine);
        System.out.println("\n");
        //1 -> 4
        printStations(interchangeStation1, interchangeStation2, 3);

        System.out.println("\n");

        getNewDirection(interchangeStation2, endStation);
        System.out.println("Take Line " + interchangeStation2 + " from -" + interchangeStation2.toUpperCase() + "- to -" + endStation.toUpperCase() + "- :");
        printStations(interchangeStation2, endStation, endLine);
        System.out.println(" ");

        count = removeDuplicates(routeStations).size();
        System.out.println("\nNumber Of Stations: " + count);
        int price = getPrice(count);
        System.out.println("Price: " + price + " EGP");
        calculateEstimatedTime(count);

        // Find all paths
        List<List<String>> allPaths = findAllPaths(startStation, endStation);
        System.out.println("\nAll possible paths from " + startStation.toUpperCase() + " to " + endStation.toUpperCase() + ":");
        int min = allPaths.get(0).size();
        int shortIndex = 0;
        for (List<String> path : allPaths) {
            count = path.size();
            if (path.size() < min) {
                min = path.size();
                shortIndex = allPaths.indexOf(path);
            }
            System.out.println(path + "\n");
        }
        System.out.println("\nShortest Route: " + allPaths.get(shortIndex));
    }

    public static List<String> findCommonStations(List<String> line1, List<String> line2) {
        List<String> commonStations = new ArrayList<>();
        for (String station : line1) {
            if (line2.contains(station)) {
                commonStations.add(station);
            }
        }
        return commonStations;
    }

    public static ArrayList<String> getLineName(int line) {
        switch (line) {
            case 1:
                return line1;
            case 2:
                return line2;
            case 3:
                return line3;
            case 4:
                return line3new;
            default:
                return null;
        }
    }


    /////////the direction of line3new is => adly mansour
    static void getNewDirection(String startStation, String endStation) {
//        if (line3new.contains(endStation) && !(line2.contains(endStation)) && !(line3.contains(endStation))) {
//            System.out.println("Direction: Towards adly mansour");
//            return;
//        }
        // Find lines for start and end stations
        List<Integer> startLines = findLines(startStation);
        List<Integer> endLines = findLines(endStation);

        // Check if both stations are on the same line
        Set<Integer> commonLines = new HashSet<>(startLines);
        commonLines.retainAll(endLines);


        if (!commonLines.isEmpty()) {
            int line = commonLines.iterator().next(); // Get the common line
            ArrayList<String> lineStations = getLineName(line);

            int startIndex = lineStations.indexOf(startStation);
            int endIndex = lineStations.indexOf(endStation);

            String direction = (startIndex < endIndex) ?
                    "Towards " + lineStations.get(lineStations.size() - 1) :
                    "Towards " + lineStations.get(0);

            System.out.println("Direction of Line " + line + ": " + direction);
        }


    }

    static void calculateEstimatedTime(int count) {
        System.out.println("Estimated Time: " + (count * 2) + " minutes.");
    }

    public static int getPrice(int numberOfStations) {
        if (numberOfStations <= 9) {
            return 6;
        } else if (numberOfStations <= 16) {
            return 8;
        } else if (numberOfStations <= 23) {
            return 12;
        } else {
            return 15;
        }
    }

    public static void printStations(String start, String end, int line) {
        ArrayList<String> lineStations = getLineName(line);
        int startIndex = lineStations.indexOf(start);
        int endIndex = lineStations.indexOf(end);

        if (startIndex <= endIndex) {
            for (int i = startIndex; i <= endIndex; i++) {
                System.out.print(lineStations.get(i));
                routeStations.add(lineStations.get(i));
                if (i < endIndex) {
                    System.out.print(" -> ");
                }
            }
        } else {
            for (int i = startIndex; i >= endIndex; i--) {
                System.out.print(lineStations.get(i));
                routeStations.add(lineStations.get(i));
                if (i > endIndex) {
                    System.out.print(" -> ");
                }
            }
        }
    }

    public static List<String> getNeighbors(String station) {
        List<String> neighbors = new ArrayList<>();

        // Check neighbors on line 1
        if (line1.contains(station)) {
            int index = line1.indexOf(station);
            if (index > 0) {
                neighbors.add(line1.get(index - 1));
            }
            if (index < line1.size() - 1) {
                neighbors.add(line1.get(index + 1));
            }
        }

        // Check neighbors on line 2
        if (line2.contains(station)) {
            int index = line2.indexOf(station);
            if (index > 0) {
                neighbors.add(line2.get(index - 1));
            }
            if (index < line2.size() - 1) {
                neighbors.add(line2.get(index + 1));
            }
        }

        // Check neighbors on line 3
        if (line3.contains(station)) {
            int index = line3.indexOf(station);
            if (index > 0) {
                neighbors.add(line3.get(index - 1));
            }
            if (index < line3.size() - 1) {
                neighbors.add(line3.get(index + 1));
            }
        }

        // Check neighbors on line 3new
        if (line3new.contains(station)) {
            int index = line3new.indexOf(station);
            if (index > 0) {
                neighbors.add(line3new.get(index - 1));
            }
            if (index < line3new.size() - 1) {
                neighbors.add(line3new.get(index + 1));
            }
        }

        return neighbors;
    }

    public static List<List<String>> findAllPaths(String start, String end) {
        List<List<String>> paths = new ArrayList<>();
        Deque<String> path = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        findAllPathsDFS(start, end, visited, path, paths);
        return paths;
    }

    private static void findAllPathsDFS(String current, String end, Set<String> visited, Deque<String> path, List<List<String>> paths) {
        visited.add(current);
        path.addLast(current);

        if (current.equals(end)) {
            paths.add(new ArrayList<>(path));
        } else {
            for (String neighbor : getNeighbors(current)) {
                if (!visited.contains(neighbor)) {
                    findAllPathsDFS(neighbor, end, visited, path, paths);
                }
            }
        }
        path.removeLast();
        visited.remove(current);
    }
}
