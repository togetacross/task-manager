package hu.mycompany.taskmanager.ui;

import hu.mycompany.taskmanager.service.TaskService;
import hu.mycompany.taskmanager.service.WorkLogService;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Gerdan Tibor
 */
public class Main {

    private final static int MAIN_MENU_OPTIONS = 5;
    private final static int TASK_MENU_OPTIONS = 7;
    private final static String[] TASK_LIST_HEADER_ARR = new String[]{"ID", "TITLE", "DESCRIPTION", "CREATED AT", "PRIORITY", "COMPLETED"};
    private final static String[] WORKLOG_LIST_HEADER_ARR = new String[]{"WORKED TIME", "FROM - TO", "COMMENT"};
    private final static int TASK_SELECT_EXIT = 0;

    public static void main(String[] args) {
        TaskService taskService = new TaskService();
        WorkLogService workLogService = new WorkLogService();
        Scanner scanner = new Scanner(System.in);
        int selected;
        do {
            printMainMenu();
            selected = selectNumber(scanner, MAIN_MENU_OPTIONS);
            switch (selected) {
                case 1:
                    Object[] taskObj = readNewTask(scanner);
                    taskService.save((String) taskObj[0], (String) taskObj[1], (int) taskObj[2]);
                    break;
                case 2:
                    Object[] taskConditionsObj = readTaskConditions(scanner);
                    List<String[]> taskArrayList = taskService.loadAllByAfterCreatedAndIsCompletedAndPriority(
                            (String) taskConditionsObj[0],
                            (boolean) taskConditionsObj[1],
                            (int) taskConditionsObj[2]
                    );
                    PrintUtil.printTable(taskArrayList, TASK_LIST_HEADER_ARR);
                    break;
                case 3:
                    PrintUtil.printTable(taskService.loadAll(), TASK_LIST_HEADER_ARR);
                    break;
                case 4:
                    int taskId = taskSelectHandler(scanner, taskService);
                    if (taskId != TASK_SELECT_EXIT) {
                        taskMenuHandler(scanner, taskService, workLogService, taskId);
                    }
                    break;
                default:
                    break;
            }
        } while (selected != MAIN_MENU_OPTIONS);
        scanner.close();
    }

    private static int taskSelectHandler(Scanner scanner, TaskService taskService) {
        printTaskMenu();
        int taskId = readNumber(scanner);
        while (!taskService.isExistsTaskId(taskId)) {
            if (taskId == TASK_SELECT_EXIT) {
                break;
            }
            getMessage("Task ID is not exsists!");
            taskId = readNumber(scanner);
        }
        return taskId;
    }

    private static void taskMenuHandler(Scanner scanner, TaskService taskService, WorkLogService workLogService, int taskId) {
        int taskSubmenuSelect;
        do {
            printTaskSubMenu();
            taskSubmenuSelect = selectNumber(scanner, TASK_MENU_OPTIONS);
            switch (taskSubmenuSelect) {
                case 1:
                    String comment = readInputText(scanner, "Comment:");
                    workLogService.start(taskId, comment);
                    break;
                case 2:
                    workLogService.finsh(taskId);
                    break;
                case 3:
                    taskService.finishTask(taskId);
                    break;
                case 4:
                    PrintUtil.printTable(workLogService.getAllByTaskId(taskId), WORKLOG_LIST_HEADER_ARR);
                    break;
                case 5:
                    String workedTime = workLogService.getAllWorkedTime(taskId);
                    System.out.println("Your Total worked time: " + workedTime);
                    break;
                case 6:
                    String answer = readInputText(scanner, "Are u sure? Y/N");
                    if (answer.toUpperCase().equals("Y")) {
                        taskService.removeTask(taskId);
                        taskSubmenuSelect = TASK_MENU_OPTIONS;
                    }
                    break;
                default:
                    break;
            }
        } while (taskSubmenuSelect != TASK_MENU_OPTIONS);

    }

    private static Object[] readTaskConditions(Scanner scanner) {
        System.lineSeparator();
        String dateText = readInputText(scanner, "From date (dd/MM/yyyy)");
        System.out.println("[1] Completed, [2] Not Completed");
        int completedSelect = selectNumber(scanner, 2);
        boolean isCompleted = completedSelect == 1;
        System.out.println("Type priority number: [1] LOW, [2] MEDIUM, [3] HIGH, [4] ALL");
        int priority = selectNumber(scanner, 4);
        return new Object[]{dateText, isCompleted, priority};
    }

    private static Object[] readNewTask(Scanner scanner) {
        System.lineSeparator();
        String title = readInputText(scanner, "Project title: ");
        String description = readInputText(scanner, "Project description: ");
        System.out.println("Select priority from [1] to [3] !");
        int priority = selectNumber(scanner, 3);
        return new Object[]{title, description, priority};
    }

    private static String readInputText(Scanner scanner, String text) {
        System.out.println(text);
        return scanner.nextLine().trim();
    }

    private static int readNumber(Scanner scanner) {
        int number = 0;
        try {
            number = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception ex) {
            getMessage("It's not a number!");
            scanner.next();
        }
        return number;
    }

    private static int selectNumber(Scanner scanner, int max) {
        int chosenNumber;
        do {
            chosenNumber = readNumber(scanner);
        } while (chosenNumber <= 0 || chosenNumber > max);
        return chosenNumber;
    }

    private static void getMessage(String text) {
        System.out.println(System.lineSeparator() + text);
    }

    private static void printMainMenu() {
        System.out.println("-------------------------------");
        System.out.println("------| What do u want? |------");
        System.out.println("[1] --- Add New Task");
        System.out.println("[2] --- List Tasks by Conditions");
        System.out.println("[3] --- List All Tasks");
        System.out.println("[4] --- Select a Task By ID");
        System.out.println("[5] --- Exit");
        System.out.println("-------------------------------");
    }

    private static void printTaskMenu() {
        System.out.println("----------------------------");
        System.out.println("[ ] --- Type a Task ID ---");
        System.out.println("[0] --- Back ---");
        System.out.println("----------------------------");
    }

    private static void printTaskSubMenu() {
        System.out.println("----------------------------");
        System.out.println("[1] --- Start count worktime");
        System.out.println("[2] --- Finish worktime");
        System.out.println("[3] --- Close Task");
        System.out.println("[4] --- View Worklogs");
        System.out.println("[5] --- View Worked Time");
        System.out.println("[6] --- Delete Task");
        System.out.println("[7] --- Back to Main menu---");
        System.out.println("----------------------------");
    }

}
