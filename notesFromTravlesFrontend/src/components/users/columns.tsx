import React, { useState, useEffect } from "react";
import { ColumnDef } from "@tanstack/react-table";
import { useToken } from "../../TokenContext";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { User, roles } from "@/User";
import { DataTable } from "./data-table";
import { Trash2 } from "lucide-react";
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
    if (!token) {
      console.error("❌ Error: Missing authentication token.");
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/api/v1/admin/users/${userId}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error(`Failed to delete user: ${response.status} ${response.statusText}`);
      }

      console.log(`✅ User with ID ${userId} deleted successfully`);

      // Fetch updated user list after successful deletion
      const updatedResponse = await fetch("http://localhost:8080/api/v1/admin/users", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      if (!updatedResponse.ok) {
        throw new Error("Failed to fetch updated user list");
      }

      const updatedUsers = await updatedResponse.json();

      // Ensure state update happens in a React component
      if (typeof setUsers === "function") {
        setUsers(updatedUsers); // Update user list
      } else {
        console.error("❌ Error: setUsers is not a valid function.");
      }

    } catch (error) {
      console.error("❌ Error deleting user:", error);
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
          <div className="flex items-center space-x-2">
            <Select
              onValueChange={(value: "USER" | "ADMIN") => updateRole(user.id, value, user.login)}
            >
              <SelectTrigger className="h-full w-25">
                <SelectValue placeholder={user.role} />
              </SelectTrigger>
              <SelectContent className="h-full w-25 p-0">
                {roles.map((role) => (
                  <SelectItem key={role} value={role} className="w-25">
                    {role}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>
        );
      }
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
