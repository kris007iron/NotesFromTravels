import React, { useState, useEffect } from "react";
import { ColumnDef } from "@tanstack/react-table";
import { useToken } from "../../TokenContext";
import { Button } from "@/components/ui/button";
import { DropdownMenu, DropdownMenuItem, DropdownMenuContent, DropdownMenuTrigger } from "@/components/ui/dropdown-menu";
import { User, roles } from "@/User";
import { DataTable } from "./data-table";
import { MoreHorizontal, Trash2 } from "lucide-react";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from "@/components/ui/alert-dialog"


export const UserTable: React.FC = () => {
  const { token } = useToken();
  const [users, setUsers] = useState<User[]>([]);

  // ✅ Function to check if an object is a valid User
  const isUser = (user: any): user is User => {
    return user && typeof user.id === "number" && typeof user.login === "string" && roles.includes(user.role);
  };

  // ✅ Fetch users only once when the component mounts
  useEffect(() => {
    const getUsers = async () => {
      try {
        const response = await fetch("http://localhost:8080/api/v1/admin/users", {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });

        if (!response.ok) {
          window.location.href = "http://localhost:5173/";
          return;
        }

        const data = await response.json();
        const validUsers: User[] = data.filter(isUser);

        setUsers(validUsers);
      } catch (error) {
        console.error("❌ Error fetching users:", error);
      }
    };

    getUsers();
  }, [token]); // ✅ Runs only when the token changes

  // ✅ Function to update user role
  const updateRole = async (userId: number, newRole: typeof roles[number], login: string) => {
    const previousUsers = [...users];
    setUsers((prevUsers) =>
      prevUsers.map((user) =>
        user.id === userId ? { ...user, role: newRole } : user
      )
    );
    //users.find()

    try {
      const response = await fetch("http://localhost:8080/api/v1/admin/users", {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ userId, login: login, role: newRole }),
      });

      if (!response.ok) {
        throw new Error("Failed to update role");
      }

      console.log(`✅ Role updated to ${newRole} for user ${userId}`);
    } catch (error) {
      console.error("❌ Error updating role:", error);
      setUsers(previousUsers); // Revert UI if request fails
    }
  };

  const deleteUser = async (userId: number) => {

    const response = await fetch("http://localhost:8080/api/v1/admin/users/" + userId, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`,
      }
    })
  }

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
                <DropdownMenuItem key={role} onClick={() => updateRole(user.id, role, user.login)}>
                  {role}
                </DropdownMenuItem>
              ))}
            </DropdownMenuContent>
          </DropdownMenu>
        );
      },
    },
    {
      id: "delete",
      header: "Delete",
      cell: ({ row }) => {
        const user = row.original;
        return (
          <AlertDialog>
            <AlertDialogTrigger>
              <Trash2 className="h-4 w-4" />
            </AlertDialogTrigger>
            <AlertDialogContent>
              <AlertDialogHeader>
                <AlertDialogTitle>Are you absolutely sure?</AlertDialogTitle>
                <AlertDialogDescription>
                  This action cannot be undone. This will permanently delete your account
                  and remove your data from our servers.
                </AlertDialogDescription>
              </AlertDialogHeader>
              <AlertDialogFooter>
                <AlertDialogCancel>Cancel</AlertDialogCancel>
                <AlertDialogAction onClick={() => deleteUser(user.id)}>Continue</AlertDialogAction>
              </AlertDialogFooter>
            </AlertDialogContent>
          </AlertDialog>
        )
      }
    }
  ];

  return <DataTable columns={columns} data={users} />;
};
