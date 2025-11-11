package dev.varev.aui.cli;

import dev.varev.aui.entity.RenderCluster;
import dev.varev.aui.entity.RenderNode;
import dev.varev.aui.service.RenderClusterService;
import dev.varev.aui.service.RenderNodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Order(2)
public class InteractiveRunner implements CommandLineRunner {

    private final RenderClusterService clusterService;
    private final RenderNodeService nodeService;
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void run(String... args) {
        System.out.println("\n╔═══════════════════════════════════════════════════╗");
        System.out.println("║  HPC Render Cluster Management System            ║");
        System.out.println("╚═══════════════════════════════════════════════════╝\n");

        boolean running = true;
        while (running) {
            displayMenu();
            String command = scanner.nextLine().trim();

            switch (command) {
                case "1" -> listClusters();
                case "2" -> listNodes();
                case "3" -> listNodesByCluster();
                case "4" -> addNode();
                case "5" -> deleteNode();
                case "6" -> displayNodeDetails();
                case "7" -> displayClusterStatistics();
                case "0" -> {
                    System.out.println("\nShutting down application...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid command. Please try again.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n┌─────────────────────────────────────────────────┐");
        System.out.println("│ Available Commands:                             │");
        System.out.println("├─────────────────────────────────────────────────┤");
        System.out.println("│ 1 - List all clusters                           │");
        System.out.println("│ 2 - List all render nodes                       │");
        System.out.println("│ 3 - List nodes by cluster                       │");
        System.out.println("│ 4 - Add new render node                         │");
        System.out.println("│ 5 - Delete render node                          │");
        System.out.println("│ 6 - Display node details                        │");
        System.out.println("│ 7 - Display cluster statistics                  │");
        System.out.println("│ 0 - Exit application                            │");
        System.out.println("└─────────────────────────────────────────────────┘");
        System.out.print("Enter command: ");
    }

    private void listClusters() {
        System.out.println("\n═══ RENDER CLUSTERS ═══");
        List<RenderCluster> clusters = clusterService.findAll();

        if (clusters.isEmpty()) {
            System.out.println("No clusters found.");
            return;
        }

        System.out.printf("%-38s %-30s %-30s%n", "ID", "Name", "Location");
        System.out.println("─".repeat(100));

        for (RenderCluster cluster : clusters) {
            System.out.printf("%-38s %-30s %-30s%n",
                    cluster.getId(),
                    cluster.getName(),
                    cluster.getLocation());
        }
        System.out.println("\nTotal clusters: " + clusters.size());
    }

    private void listNodes() {
        System.out.println("\n═══ RENDER NODES ═══");
        List<RenderNode> nodes = nodeService.findAll();

        if (nodes.isEmpty()) {
            System.out.println("No nodes found.");
            return;
        }

        System.out.printf("%-38s %-35s %-12s %-12s%n",
                "ID", "Hostname", "GPU Load %", "Temp °C");
        System.out.println("─".repeat(100));

        for (RenderNode node : nodes) {
            System.out.printf("%-38s %-35s %-12.1f %-12d%n",
                    node.getId(),
                    node.getHostname(),
                    node.getGpuLoad(),
                    node.getTemperature());
        }
        System.out.println("\nTotal nodes: " + nodes.size());
    }

    private void listNodesByCluster() {
        System.out.print("\nEnter cluster name: ");
        String clusterName = scanner.nextLine().trim();

        Optional<RenderCluster> clusterOpt = clusterService.findByName(clusterName);

        if (clusterOpt.isEmpty()) {
            System.out.println("Cluster not found: " + clusterName);
            return;
        }

        RenderCluster cluster = clusterOpt.get();
        List<RenderNode> nodes = nodeService.findByCluster(cluster);

        System.out.println("\n═══ NODES IN CLUSTER: " + cluster.getName() + " ═══");

        if (nodes.isEmpty()) {
            System.out.println("No nodes in this cluster.");
            return;
        }

        System.out.printf("%-38s %-35s %-12s %-12s%n",
                "ID", "Hostname", "GPU Load %", "Temp °C");
        System.out.println("─".repeat(100));

        for (RenderNode node : nodes) {
            System.out.printf("%-38s %-35s %-12.1f %-12d%n",
                    node.getId(),
                    node.getHostname(),
                    node.getGpuLoad(),
                    node.getTemperature());
        }
        System.out.println("\nTotal nodes in cluster: " + nodes.size());
    }

    private void addNode() {
        System.out.println("\n═══ ADD NEW RENDER NODE ═══");

        // Display available clusters
        List<RenderCluster> clusters = clusterService.findAll();
        if (clusters.isEmpty()) {
            System.out.println("No clusters available. Please create a cluster first.");
            return;
        }

        System.out.println("\nAvailable clusters:");
        for (int i = 0; i < clusters.size(); i++) {
            RenderCluster cluster = clusters.get(i);
            System.out.printf("%d. %s (%s)%n", i + 1, cluster.getName(), cluster.getLocation());
        }

        System.out.print("\nSelect cluster (1-" + clusters.size() + "): ");
        int clusterChoice;
        try {
            clusterChoice = Integer.parseInt(scanner.nextLine().trim());
            if (clusterChoice < 1 || clusterChoice > clusters.size()) {
                System.out.println("Invalid cluster selection.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        RenderCluster selectedCluster = clusters.get(clusterChoice - 1);

        System.out.print("Enter hostname: ");
        String hostname = scanner.nextLine().trim();

        System.out.print("Enter GPU load (0-100) [above 100 means over: ");
        double gpuLoad;
        try {
            gpuLoad = Double.parseDouble(scanner.nextLine().trim());
            if (gpuLoad < 0) {
                System.out.println("GPU load must be a not negative number.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid GPU load value.");
            return;
        }

        System.out.print("Enter temperature (°C): ");
        int temperature;
        try {
            temperature = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid temperature value.");
            return;
        }

        RenderNode newNode = RenderNode.builder()
                .id(UUID.randomUUID())
                .hostname(hostname)
                .gpuLoad(gpuLoad)
                .temperature(temperature)
                .cluster(selectedCluster)
                .build();

        nodeService.save(newNode);

        System.out.println("\n✓ Render node added successfully!");
        System.out.println("ID: " + newNode.getId());
        System.out.println("Hostname: " + newNode.getHostname());
        System.out.println("Cluster: " + selectedCluster.getName());
    }

    private void deleteNode() {
        System.out.print("\nEnter node ID to delete: ");
        String idString = scanner.nextLine().trim();

        UUID nodeId;
        try {
            nodeId = UUID.fromString(idString);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format.");
            return;
        }

        Optional<RenderNode> nodeOpt = nodeService.findById(nodeId);

        if (nodeOpt.isEmpty()) {
            System.out.println("Node not found with ID: " + nodeId);
            return;
        }

        RenderNode node = nodeOpt.get();
        System.out.println("\nNode to delete:");
        System.out.println("Hostname: " + node.getHostname());
//        System.out.println("Cluster: " + node.getCluster().getName());

        System.out.print("\nAre you sure you want to delete this node? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equals("yes") || confirmation.equals("y")) {
            nodeService.deleteById(nodeId);
            System.out.println("✓ Node deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private void displayNodeDetails() {
        System.out.print("\nEnter node ID: ");
        String idString = scanner.nextLine().trim();

        UUID nodeId;
        try {
            nodeId = UUID.fromString(idString);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format.");
            return;
        }

        Optional<RenderNode> nodeOpt = nodeService.findById(nodeId);

        if (nodeOpt.isEmpty()) {
            System.out.println("Node not found with ID: " + nodeId);
            return;
        }

        RenderNode node = nodeOpt.get();

        System.out.println("\n═══ NODE DETAILS ═══");
        System.out.println("ID:          " + node.getId());
        System.out.println("Hostname:    " + node.getHostname());
        System.out.println("GPU Load:    " + node.getGpuLoad() + "%");
        System.out.println("Temperature: " + node.getTemperature() + "°C");
        System.out.println("Cluster:     " + node.getCluster().getName());
        System.out.println("Location:    " + node.getCluster().getLocation());

        // Performance status
        System.out.println("\n--- Status ---");
        if (node.getGpuLoad() > 80) {
            System.out.println("⚠ GPU Load: HIGH");
        } else if (node.getGpuLoad() > 60) {
            System.out.println("⚡ GPU Load: MODERATE");
        } else {
            System.out.println("✓ GPU Load: NORMAL");
        }

        if (node.getTemperature() > 75) {
            System.out.println("⚠ Temperature: HIGH");
        } else if (node.getTemperature() > 65) {
            System.out.println("⚡ Temperature: MODERATE");
        } else {
            System.out.println("✓ Temperature: NORMAL");
        }
    }

    private void displayClusterStatistics() {
        System.out.println("\n═══ CLUSTER STATISTICS ═══");
        List<RenderCluster> clusters = clusterService.findAll();

        if (clusters.isEmpty()) {
            System.out.println("No clusters found.");
            return;
        }

        for (RenderCluster cluster : clusters) {
            List<RenderNode> nodes = nodeService.findByCluster(cluster);

            System.out.println("\n┌─ " + cluster.getName() + " ─");
            System.out.println("│ Location: " + cluster.getLocation());
            System.out.println("│ Total Nodes: " + nodes.size());

            if (!nodes.isEmpty()) {
                double avgGpuLoad = nodes.stream()
                        .mapToDouble(RenderNode::getGpuLoad)
                        .average()
                        .orElse(0.0);

                double avgTemp = nodes.stream()
                        .mapToInt(RenderNode::getTemperature)
                        .average()
                        .orElse(0.0);

                long overloadedNodes = nodes.stream()
                        .filter(n -> n.getGpuLoad() > 80)
                        .count();

                long overheatedNodes = nodes.stream()
                        .filter(n -> n.getTemperature() > 75)
                        .count();

                System.out.printf("│ Avg GPU Load: %.1f%%%n", avgGpuLoad);
                System.out.printf("│ Avg Temperature: %.1f°C%n", avgTemp);
                System.out.println("│ Overloaded Nodes: " + overloadedNodes);
                System.out.println("│ Overheated Nodes: " + overheatedNodes);
            }
            System.out.println("└─────────────────────────");
        }

        System.out.println("\nTotal System Statistics:");
        System.out.println("- Total Clusters: " + clusters.size());
        System.out.println("- Total Nodes: " + nodeService.count());
    }
}