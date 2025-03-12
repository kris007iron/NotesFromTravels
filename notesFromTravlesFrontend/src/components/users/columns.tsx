import { ColumnDef } from "@tanstack/react-table";
import { MoreHorizontal } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
    DropdownMenu,
    DropdownMenuItem,
    DropdownMenuContent,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { User, roles, users as mockUsers } from "@/User"; // ✅ Import mock data
import { useState } from "react";
import { DataTable } from "./data-table";

export const UserTable = () => {
    // ✅ Use mock users as initial state
    const [users, setUsers] = useState<User[]>(mockUsers);

    // ✅ Function to update user role
    const updateRole = async (userId: number, newRole: typeof roles[number]) => {
        // ✅ Optimistically update UI
        const previousUsers = [...users];
        setUsers((prevUsers) =>
            prevUsers.map((user) =>
                user.id === userId ? { ...user, role: newRole } : user
            )
        );

        try {
            const response = await fetch(`http://localhost:8080/api/v1/admin/users/${userId}/role`, {
                method: "PATCH",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer YOUR_TOKEN_HERE`, // Replace with actual token
                },
                body: JSON.stringify({ role: newRole }),
            });

            if (!response.ok) {
                throw new Error("Failed to update role");
            }
            console.log(`✅ Role updated to ${newRole} for user ${userId}`);
        } catch (error) {
            console.error("❌ Error updating role:", error);
            setUsers(previousUsers); // ❌ Revert to old data if API fails
        }
    };

    // ✅ Define table columns
    const columns: ColumnDef<User>[] = [
        { accessorKey: "id", header: "ID" },
        { accessorKey: "login", header: "Login" },
        {
            accessorKey: "role",
            header: "Role",
            id: "role",
            cell: ({ row }) => {
                const user = row.original;

                return (
                    <DropdownMenu>
                        <DropdownMenuTrigger asChild>
                            <Button variant="default" className="h-8 w-24 p-0">
                                <span>{user.role}</span>
                                <MoreHorizontal className="h-4 w-4" />
                            </Button>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent align="end">
                            {roles.map((role) => (
                                <DropdownMenuItem
                                    key={role}
                                    onClick={() => updateRole(user.id, role)}
                                >
                                    {role}
                                </DropdownMenuItem>
                            ))}
                        </DropdownMenuContent>
                    </DropdownMenu>
                );
            },
        },
    ];

    return (
        <DataTable columns={columns} data={users} />
    );
};
